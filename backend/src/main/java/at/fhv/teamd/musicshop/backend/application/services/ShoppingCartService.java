package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.medium.Stock;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static at.fhv.teamd.musicshop.backend.application.services.DTOProvider.buildShoppingCartDTO;

public class ShoppingCartService {
    private static final Map<String, Set<LineItem>> sessionLineItems = new HashMap<>();

    private static MediumRepository mediumRepository;
    private static ArticleRepository articleRepository;

    ShoppingCartService() {
        mediumRepository = RepositoryFactory.getMediumRepositoryInstance();
        articleRepository = RepositoryFactory.getArticleRepositoryInstance();
    }

    private void initializeShoppingcart(String userId) {
        if (!shoppingCartExists(userId)) {
            sessionLineItems.put(userId, new HashSet<>());
        } else {
            throw new IllegalStateException("Shopping cart already exists.");
        }
    }

    public boolean addToShoppingCart(String userId, MediumDTO mediumDTO, int amount) {
        if (!shoppingCartExists(userId)) {
            initializeShoppingcart(userId);
        }

        if (amount < 1) return false;

        AtomicBoolean success = new AtomicBoolean(true);

        Set<LineItem> lineItems = sessionLineItems.get(userId);

        Medium medium = mediumRepository.findMediumById(mediumDTO.id()).orElseThrow();

        lineItems.stream()
                .filter(li -> li.getMedium().getId() == mediumDTO.id())
                .findFirst()
                .ifPresentOrElse(li -> {
                    if (li.getQuantity().getValue() + amount <= medium.getStock().getQuantity().getValue()) {
                        li.increaseQuantity(Quantity.of(amount));
                    } else {
                        success.set(false);
                    }
                }, () -> {
                    if (amount <= medium.getStock().getQuantity().getValue()) {
                        lineItems.add(new LineItem(Quantity.of(amount), medium));
                        sessionLineItems.put(userId, lineItems);
                    } else {
                        success.set(false);
                    }
                });
        return success.get();
    }

    public boolean removeFromShoppingCart(String userId, MediumDTO mediumDTO, int amount) {
        if (!shoppingCartExists(userId)) {
            emptyShoppingCart(userId);
        }

        if (amount < 1) return false;

        AtomicBoolean success = new AtomicBoolean(true);

        Set<LineItem> lineItems = sessionLineItems.get(userId);

        Optional<LineItem> lineItem = lineItems.stream()
                .filter(li -> li.getMedium().getId() == mediumDTO.id())
                .findFirst();

        lineItem.ifPresentOrElse(li -> {
            if (li.getQuantity().getValue() > amount) {
                li.decreaseQuantity(Quantity.of(amount));
            } else {
                lineItems.remove(li);
            }
        }, () -> success.set(false));
        return success.get();
    }

    public void emptyShoppingCart(String userId) {
        sessionLineItems.put(userId, new HashSet<>());
    }

    public ShoppingCartDTO getShoppingCart(String userId) {
        if (!shoppingCartExists(userId)) {
            initializeShoppingcart(userId);
        }
        return buildShoppingCartDTO(articleRepository, mediumRepository, sessionLineItems.get(userId));
    }

    // TODO: specific exception
    public boolean buyFromShoppingCart(String userId, int id) {
        if (!shoppingCartExists(userId)) {
            emptyShoppingCart(userId);
        }

        Set<LineItem> lineItems = sessionLineItems.get(userId);

        if (lineItems.isEmpty()) return false;

        lineItems.forEach(lineItem -> {
            Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();
            if (medium.getStock().getQuantity().getValue() < lineItem.getQuantity().getValue()) {
                throw new RuntimeException("not enough in Stock: " + medium.getId());
            }
        });

        try {
            ServiceFactory.getInvoiceServiceInstance().createInvoice(lineItems, id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        emptyShoppingCart(userId);

        // decrease quantities
        lineItems.forEach(lineItem -> {
            Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();
            medium.setStock(Stock.of(medium.getStock().getQuantity().decreaseBy(lineItem.getQuantity())));
            mediumRepository.update(medium);
        });
        return true;
    }

    private boolean shoppingCartExists(String userId) {
        return sessionLineItems.containsKey(userId);
    }
}
