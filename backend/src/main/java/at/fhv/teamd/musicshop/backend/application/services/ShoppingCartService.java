package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.invoice.PaymentMethod;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;

import java.util.*;

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

    // TODO: notification on client
    public void addToShoppingCart(UUID sessionUUID, MediumDTO mediumDTO, int amount) {
        if (!shoppingCartExists(sessionUUID)) {
            initializeShoppingcart(sessionUUID);
        }
        Set<LineItem> lineItems = sessionLineItems.get(sessionUUID);

        Medium medium = mediumRepository.findMediumById(mediumDTO.id()).orElseThrow();

        lineItems.stream()
                .filter(li -> li.getMedium().getId() == mediumDTO.id())
                .findFirst()
                .ifPresentOrElse(li -> {
                    if (li.getQuantity().getValue() + amount <= medium.getStock().getQuantity().getValue()) {
                        li.increaseQuantity(Quantity.of(amount));
                    }
                }, () -> {
                    if (amount <= medium.getStock().getQuantity().getValue()) {
                        lineItems.add(new LineItem(Quantity.of(amount), medium));
                        sessionLineItems.put(sessionUUID, lineItems);
                    }
                });
    }

    public void removeFromShoppingCart(UUID sessionUUID, MediumDTO mediumDTO, int amount) {
        if (!shoppingCartExists(sessionUUID)) {
            emptyShoppingCart(sessionUUID);
        }

        Set<LineItem> lineItems = sessionLineItems.get(sessionUUID);

        LineItem lineItem = lineItems.stream()
                .filter(li -> li.getMedium().getId() == mediumDTO.id())
                .findFirst()
                .orElseThrow();

        if (lineItem.getQuantity().getValue() > amount && amount > 0) {
            lineItem.decreaseQuantity(Quantity.of(amount));
        } else {
            lineItems.remove(lineItem);
        }
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
    public boolean buyFromShoppingCart(UUID sessionUUID, int id) {
        if (!shoppingCartExists(sessionUUID)) {
            emptyShoppingCart(sessionUUID);
        }

        Set<LineItem> lineItems = sessionLineItems.get(sessionUUID);
        lineItems.forEach(lineItem -> {
            Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();
            if (medium.getStock().getQuantity().getValue() < lineItem.getQuantity().getValue()) {
                throw new RuntimeException("not enough in Stock: " + medium.getId());
            }
        });

        try {
            ServiceFactory.getInvoiceServiceInstance().createInvoice(PaymentMethod.CASH, lineItems);
//            ServiceFactory.getInvoiceServiceInstance().createInvoice(PaymentMethod.CASH, lineItems, new Customer("Lukas", "Kaufmann", 1000000));
        } catch (Exception e) {
            e.printStackTrace();
        }

        emptyShoppingCart(sessionUUID);

        // decrease quantities
        lineItems.forEach(lineItem -> {
            Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();
            medium.getStock().getQuantity().decreaseBy(lineItem.getQuantity());
        });
        return true;
    }

    private boolean shoppingCartExists(UUID sessionUUID) {
        return sessionLineItems.containsKey(sessionUUID);
    }
}
