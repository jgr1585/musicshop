package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.invoice.PaymentMethod;
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
    private static final Map<UUID, Set<LineItem>> sessionLineItems = new HashMap<>();

    private static MediumRepository mediumRepository;
    private static ArticleRepository articleRepository;

    ShoppingCartService() {
        mediumRepository = RepositoryFactory.getMediumRepositoryInstance();
        articleRepository = RepositoryFactory.getArticleRepositoryInstance();
    }

    private void initializeShoppingcart(UUID sessionUUID) {
        if (!shoppingCartExists(sessionUUID)) {
            sessionLineItems.put(sessionUUID, new HashSet<>());
        } else {
            throw new IllegalStateException("Shopping cart already exists.");
        }
    }

    public boolean addToShoppingCart(UUID sessionUUID, MediumDTO mediumDTO, int amount) {
        if (!shoppingCartExists(sessionUUID)) {
            initializeShoppingcart(sessionUUID);
        }

        AtomicBoolean success = new AtomicBoolean(true);

        Set<LineItem> lineItems = sessionLineItems.get(sessionUUID);

        Medium medium = mediumRepository.findMediumById(mediumDTO.id()).orElseThrow();

        lineItems.stream()
                .filter(li -> li.getMedium().getId() == mediumDTO.id())
                .findFirst()
                .ifPresentOrElse(li -> {
                    if (li.getQuantity().getValue() + amount <= medium.getStock().getQuantity().getValue() && amount > 0) {
                        li.increaseQuantity(Quantity.of(amount));
                    } else {
                        success.set(false);
                    }
                }, () -> {
                    if (amount <= medium.getStock().getQuantity().getValue()) {
                        lineItems.add(new LineItem(Quantity.of(amount), medium));
                        sessionLineItems.put(sessionUUID, lineItems);
                    } else {
                        success.set(false);
                    }
                });
        return success.get();
    }

    public boolean removeFromShoppingCart(UUID sessionUUID, MediumDTO mediumDTO, int amount) {
        if (!shoppingCartExists(sessionUUID)) {
            emptyShoppingCart(sessionUUID);
        }

        AtomicBoolean success = new AtomicBoolean(true);

        Set<LineItem> lineItems = sessionLineItems.get(sessionUUID);

        Optional<LineItem> lineItem = lineItems.stream()
                .filter(li -> li.getMedium().getId() == mediumDTO.id())
                .findFirst();

        lineItem.ifPresentOrElse(li -> {
            if (li.getQuantity().getValue() > amount && amount > 0) {
                li.decreaseQuantity(Quantity.of(amount));
            } else {
                lineItems.remove(li);
            }
        }, () -> success.set(false));
        return success.get();
    }

    public void emptyShoppingCart(UUID sessionUUID) {
        sessionLineItems.put(sessionUUID, new HashSet<>());
    }

    public ShoppingCartDTO getShoppingCart(UUID sessionUUID) {
        if (!shoppingCartExists(sessionUUID)) {
            initializeShoppingcart(sessionUUID);
        }
        return buildShoppingCartDTO(articleRepository, mediumRepository, sessionLineItems.get(sessionUUID));
    }

    // TODO: append paymentMethod
    // TODO: specific exception
    // TODO: quantity not updated
    public boolean buyFromShoppingCart(UUID sessionUUID, int id) {
        if (!shoppingCartExists(sessionUUID)) {
            emptyShoppingCart(sessionUUID);
        }

        Set<LineItem> lineItems = sessionLineItems.get(sessionUUID);

        if (lineItems.isEmpty()) return false;

        lineItems.forEach(lineItem -> {
            Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();
            if (medium.getStock().getQuantity().getValue() < lineItem.getQuantity().getValue()) {
                throw new RuntimeException("not enough in Stock: " + medium.getId());
            }
        });

        try {
            ServiceFactory.getInvoiceServiceInstance().createInvoice(PaymentMethod.CASH, lineItems, id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        emptyShoppingCart(sessionUUID);

        // decrease quantities
        lineItems.forEach(lineItem -> {
            Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();
//            medium.getStock().setQuantity(medium.getStock().getQuantity().decreaseBy(lineItem.getQuantity()));
            medium.setStock(Stock.of(medium.getStock().getQuantity().decreaseBy(lineItem.getQuantity())));
            mediumRepository.update(medium);
        });
        return true;
    }

    private boolean shoppingCartExists(UUID sessionUUID) {
        return sessionLineItems.containsKey(sessionUUID);
    }
}
