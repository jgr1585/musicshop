package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.medium.Stock;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;
import at.fhv.teamd.musicshop.library.exceptions.ShoppingCartException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static at.fhv.teamd.musicshop.backend.application.services.DTOProvider.buildShoppingCartDTO;

public class ShoppingCartService {
    private static final Map<String, Set<LineItem>> sessionLineItems = new HashMap<>();

    private static final MediumRepository mediumRepository = RepositoryFactory.getMediumRepositoryInstance();

    private String message = "Quantity not acceptable";

    private void initializeShoppingcart(String userId) {
        if (!shoppingCartExists(userId)) {
            sessionLineItems.put(userId, new HashSet<>());
        }
    }

    public void addToShoppingCart(String userId, long mediumId, int amount) throws ShoppingCartException {
        if (!shoppingCartExists(userId)) {
            initializeShoppingcart(userId);
        }

        if (amount < 1) throw new ShoppingCartException(message);

        Set<LineItem> lineItems = sessionLineItems.get(userId);

        Medium medium = mediumRepository.findMediumById(mediumId).orElseThrow();

        if (lineItems.stream().anyMatch(li -> li.getMedium().getId() == mediumId)) {
            LineItem lineItem = lineItems.stream().filter(li -> li.getMedium().getId() == mediumId).findFirst().get();
            if (lineItem.getQuantity().getValue() + amount <= medium.getStock().getQuantity().getValue()) {
                lineItem.increaseQuantity(Quantity.of(amount));
            } else {
                throw new ShoppingCartException(message);
            }
        } else {
            if (amount <= medium.getStock().getQuantity().getValue()) {
                lineItems.add(new LineItem(Quantity.of(amount), medium));
                sessionLineItems.put(userId, lineItems);
            } else {
                throw new ShoppingCartException(message);
            }
        }
    }

    public void removeFromShoppingCart(String userId, long mediumId, int amount) throws ShoppingCartException {
        if (!shoppingCartExists(userId)) {
            emptyShoppingCart(userId);
        }

        if (amount < 1) throw new ShoppingCartException(message);

        Set<LineItem> lineItems = sessionLineItems.get(userId);

        LineItem lineItem = lineItems.stream()
                .filter(li -> li.getMedium().getId() == mediumId)
                .findFirst()
                .orElseThrow(() -> new ShoppingCartException(message));

        if (lineItem.getQuantity().getValue() > amount) {
            lineItem.decreaseQuantity(Quantity.of(amount));
        } else {
            lineItems.remove(lineItem);
        }
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

    public String buyFromShoppingCart(String userId, int id) throws ShoppingCartException {
        if (!shoppingCartExists(userId)) {
            emptyShoppingCart(userId);
        }

        Set<LineItem> lineItems = sessionLineItems.get(userId);

        if (lineItems.isEmpty()) throw new ShoppingCartException("No LineItems in ShoppingCart");

        for (LineItem lineItem : lineItems) {
            Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();
            if (medium.getStock().getQuantity().getValue() < lineItem.getQuantity().getValue()) {
                throw new ShoppingCartException("Not enough Items in Stock: " + medium.getId());
            }
        }

        Long invoiceNo = ServiceFactory.getInvoiceServiceInstance().createInvoice(lineItems, id);

        emptyShoppingCart(userId);

        // decrease quantities
        for (LineItem lineItem : lineItems) {
            Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();
            medium.setStock(Stock.of(medium.getStock().getQuantity().decreaseBy(lineItem.getQuantity())));
            mediumRepository.update(medium);
        }
        return String.valueOf(invoiceNo);
    }

    public void addDigitalsToShoppingCart(String userId, long mediumId) throws ShoppingCartException {
        if (!shoppingCartExists(userId)) {
            initializeShoppingcart(userId);
        }

        Set<LineItem> lineItems = sessionLineItems.get(userId);

        Medium medium = mediumRepository.findMediumById(mediumId).orElseThrow();

        if (medium.getType() != MediumType.DIGITAL)
            throw new ShoppingCartException("Only digital mediums are allowed here");

        if (lineItems.stream().noneMatch(li -> li.getMedium().getId() == mediumId)) {
            lineItems.add(new LineItem(Quantity.of(1), medium));
            sessionLineItems.put(userId, lineItems);
        } else {
            throw new ShoppingCartException("Already in ShoppingCart");
        }
    }

    public void removeDigitalsFromShoppingCart(String userId, long mediumId) throws ShoppingCartException {
        if (!shoppingCartExists(userId)) {
            emptyShoppingCart(userId);
        }

        Set<LineItem> lineItems = sessionLineItems.get(userId);

        lineItems.remove(
                lineItems.stream()
                        .filter(li -> li.getMedium().getId() == mediumId)
                        .findFirst()
                        .orElseThrow(() -> new ShoppingCartException("Not in ShoppingCart")));
    }

    public String buyDigitalsFromShoppingCart(String userId, String creditCardNo) throws CustomerNotFoundException, ShoppingCartException {
        if (!shoppingCartExists(userId)) {
            emptyShoppingCart(userId);
        }

        if (!ServiceFactory.getCustomerServiceInstance().getCustomerByUsername(userId).getCreditcardNo().equals(creditCardNo)) {
            throw new ShoppingCartException("CreditCardNo does not match!");
        }

        int id = ServiceFactory.getCustomerServiceInstance().getCustomerNoByUsername(userId);

        Set<LineItem> lineItems = sessionLineItems.get(userId);

        if (lineItems.isEmpty()) throw new ShoppingCartException("No LineItems in ShoppingCart");

        for (LineItem lineItem : lineItems) {
            Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();
            if (medium.getType() != MediumType.DIGITAL)
                throw new ShoppingCartException("Only digital mediums are allowed");
        }

        Long invoiceNo = ServiceFactory.getInvoiceServiceInstance().createInvoice(lineItems, id);

        emptyShoppingCart(userId);

        return String.valueOf(invoiceNo);
    }

    private boolean shoppingCartExists(String userId) {
        return sessionLineItems.containsKey(userId);
    }
}
