package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.Stock;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static at.fhv.teamd.musicshop.backend.application.services.DTOProvider.buildShoppingCartDTO;

public class ShoppingCartService {
    private static final Map<String, Set<LineItem>> sessionLineItems = new HashMap<>();

    private static MediumRepository mediumRepository;

    ShoppingCartService() {
        mediumRepository = RepositoryFactory.getMediumRepositoryInstance();
    }

    private void initializeShoppingcart(String userId) {
        if (!shoppingCartExists(userId)) {
            sessionLineItems.put(userId, new HashSet<>());
        } else {
            throw new IllegalStateException("Shopping cart already exists.");
        }
    }

    public void addToShoppingCart(String userId, MediumDTO mediumDTO, int amount) {
        if (!shoppingCartExists(userId)) {
            initializeShoppingcart(userId);
        }

        if (amount < 1) throw new IllegalArgumentException("Quantity not acceptable");

        Set<LineItem> lineItems = sessionLineItems.get(userId);

        Medium medium = mediumRepository.findMediumById(mediumDTO.id()).orElseThrow();

        lineItems.stream()
                .filter(li -> li.getMedium().getId() == mediumDTO.id())
                .findFirst()
                .ifPresentOrElse(li -> {
                    if (li.getQuantity().getValue() + amount <= medium.getStock().getQuantity().getValue()) {
                        li.increaseQuantity(Quantity.of(amount));
                    } else {
                        throw new IllegalArgumentException("Quantity not acceptable");
                    }
                }, () -> {
                    if (amount <= medium.getStock().getQuantity().getValue()) {
                        lineItems.add(new LineItem(Quantity.of(amount), medium));
                        sessionLineItems.put(userId, lineItems);
                    } else {
                        throw new IllegalArgumentException("Quantity not acceptable");
                    }
                });
    }

    public void removeFromShoppingCart(String userId, MediumDTO mediumDTO, int amount) {
        if (!shoppingCartExists(userId)) {
            emptyShoppingCart(userId);
        }

        if (amount < 1) throw new IllegalArgumentException("Quantity not acceptable");

        Set<LineItem> lineItems = sessionLineItems.get(userId);

        lineItems.stream()
                .filter(li -> li.getMedium().getId() == mediumDTO.id())
                .findFirst()
                .ifPresentOrElse(li -> {
                    if (li.getQuantity().getValue() > amount) {
                        li.decreaseQuantity(Quantity.of(amount));
                    } else {
                        lineItems.remove(li);
                    }
                }, () -> {
                    throw new IllegalArgumentException("Quantity not acceptable");
                });
    }

    public void emptyShoppingCart(String userId) {
        sessionLineItems.put(userId, new HashSet<>());
    }

    public ShoppingCartDTO getShoppingCart(String userId) {
        if (!shoppingCartExists(userId)) {
            initializeShoppingcart(userId);
        }
        return buildShoppingCartDTO(sessionLineItems.get(userId));
    }

    public String buyFromShoppingCart(String userId, int id) {
        if (!shoppingCartExists(userId)) {
            emptyShoppingCart(userId);
        }

        Set<LineItem> lineItems = sessionLineItems.get(userId);

        if (lineItems.isEmpty()) throw new IllegalArgumentException("No LineItems in ShoppingCart");

        lineItems.forEach(lineItem -> {
            Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();
            if (medium.getStock().getQuantity().getValue() < lineItem.getQuantity().getValue()) {
                throw new RuntimeException("Not enough Items in Stock: " + medium.getId());
            }
        });

        Long invoiceNo = ServiceFactory.getInvoiceServiceInstance().createInvoice(lineItems, id);

        emptyShoppingCart(userId);

        // decrease quantities
        lineItems.forEach(lineItem -> {
            Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();
            medium.setStock(Stock.of(medium.getStock().getQuantity().decreaseBy(lineItem.getQuantity())));
            mediumRepository.update(medium);
        });
        return String.valueOf(invoiceNo);
    }

    private boolean shoppingCartExists(String userId) {
        return sessionLineItems.containsKey(userId);
    }
}
