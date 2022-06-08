package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.medium.Stock;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.dto.ShoppingCartDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;
import at.fhv.teamd.musicshop.library.exceptions.ShoppingCartException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static at.fhv.teamd.musicshop.backend.application.services.DTOProvider.buildShoppingCartDTO;

public class ShoppingCartService {
    private final Map<String, Set<LineItem>> physicalMediumLineItems = new HashMap<>();
    private final Map<String, Set<LineItem>> digitalMediumLineItems = new HashMap<>();

    private final MediumRepository mediumRepository = RepositoryFactory.getMediumRepositoryInstance();

    private static final String ERR_MSG_QUANTITY = "Quantity not acceptable";
    private static final String ERR_MSG_INSUFFICIENT_STOCK = "Insufficient in stock";

    private void initializeUserShoppingCartsIfUninitialized(String userId) {
        if (!userShoppingCartsExist(userId)) {
            physicalMediumLineItems.put(userId, new HashSet<>());
            digitalMediumLineItems.put(userId, new HashSet<>());
        }
    }

    private boolean userShoppingCartsExist(String userId) {
        return digitalMediumLineItems.containsKey(userId) && physicalMediumLineItems.containsKey(userId);
    }

    public void addPhysicalMediumToShoppingCart(String userId, long mediumId, int amount) throws ShoppingCartException {
        initializeUserShoppingCartsIfUninitialized(userId);

        if (amount < 1) throw new ShoppingCartException(ERR_MSG_QUANTITY);

        Set<LineItem> lineItems = physicalMediumLineItems.get(userId);

        Medium medium = mediumRepository.findMediumById(mediumId).orElseThrow(() -> new ShoppingCartException("Medium not found."));

        if (medium.getType() == MediumType.DIGITAL)
            throw new ShoppingCartException("Only non-digital mediums are allowed here");

        // medium already exists in lineItems -> increase lineItem amount
        if (lineItems.stream().anyMatch(li -> li.getMedium().getId() == mediumId)) {
            LineItem lineItem = lineItems.stream().filter(li -> li.getMedium().getId() == mediumId).findFirst().get();

            // insufficient mediums in stock
            if (lineItem.getQuantity().getValue() + amount > medium.getStock().getQuantity().getValue())
                throw new ShoppingCartException(ERR_MSG_INSUFFICIENT_STOCK);

            lineItem.increaseQuantity(Quantity.of(amount));

            // medium does not exists in lineItems
        } else {
            // insufficient mediums in stock
            if (amount > medium.getStock().getQuantity().getValue())
                throw new ShoppingCartException(ERR_MSG_INSUFFICIENT_STOCK);

            lineItems.add(new LineItem(Quantity.of(amount), medium));
        }
    }

    public void addDigitalMediumToShoppingCart(String userId, long mediumId) throws ShoppingCartException {
        initializeUserShoppingCartsIfUninitialized(userId);

        Set<LineItem> lineItems = digitalMediumLineItems.get(userId);

        Medium medium = mediumRepository.findMediumById(mediumId).orElseThrow(() -> new ShoppingCartException("Medium not found."));

        if (medium.getType() != MediumType.DIGITAL)
            throw new ShoppingCartException("Only digital mediums are allowed here");

        // medium does not exists in lineItems
        if (lineItems.stream().noneMatch(li -> li.getMedium().getId() == mediumId)) {
            lineItems.add(new LineItem(Quantity.of(1), medium));

            // medium already exists in lineItems
        } else {
            throw new ShoppingCartException("Already in ShoppingCart");
        }
    }

    public void removePhysicalMediumFromShoppingCart(String userId, long mediumId, int amount) throws ShoppingCartException {
        initializeUserShoppingCartsIfUninitialized(userId);

        if (amount < 1) throw new ShoppingCartException(ERR_MSG_QUANTITY);

        Set<LineItem> lineItems = physicalMediumLineItems.get(userId);

        LineItem lineItem = lineItems.stream()
                .filter(li -> li.getMedium().getId() == mediumId)
                .findFirst()
                // no line item with medium found
                .orElseThrow(() -> new ShoppingCartException("No line item with medium exists"));

        // decrease amount or remove lineItem
        if (lineItem.getQuantity().getValue() > amount) {
            lineItem.decreaseQuantity(Quantity.of(amount));
        } else {
            lineItems.remove(lineItem);
        }
    }

    public void removeDigitalMediumFromShoppingCart(String userId, long mediumId) throws ShoppingCartException {
        initializeUserShoppingCartsIfUninitialized(userId);

        Set<LineItem> lineItems = digitalMediumLineItems.get(userId);

        lineItems.remove(
                lineItems.stream()
                        .filter(li -> li.getMedium().getId() == mediumId)
                        .findFirst()
                        .orElseThrow(() -> new ShoppingCartException("No line item with medium exists")));
    }

    public void emptyPhysicalMediumShoppingCart(String userId) {
        physicalMediumLineItems.put(userId, new HashSet<>());
    }

    public void emptyDigitalMediumShoppingCart(String userId) {
        digitalMediumLineItems.put(userId, new HashSet<>());
    }

    public ShoppingCartDTO getPhysicalMediumShoppingCart(String userId) {
        initializeUserShoppingCartsIfUninitialized(userId);

        return buildShoppingCartDTO(physicalMediumLineItems.get(userId));
    }

    public ShoppingCartDTO getDigitalMediumShoppingCart(String userId) {
        initializeUserShoppingCartsIfUninitialized(userId);

        return buildShoppingCartDTO(digitalMediumLineItems.get(userId));
    }

    public String buyFromPhysicalMediumShoppingCart(String userId, int customerNo) throws ShoppingCartException {
        initializeUserShoppingCartsIfUninitialized(userId);

        Set<LineItem> lineItems = physicalMediumLineItems.get(userId);

        if (lineItems.isEmpty()) throw new ShoppingCartException("No LineItems in ShoppingCart");

        for (LineItem lineItem : lineItems) {
            Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();

            // insufficient mediums in stock
            if (medium.getStock().getQuantity().getValue() < lineItem.getQuantity().getValue()) {
                throw new ShoppingCartException(ERR_MSG_INSUFFICIENT_STOCK);
            }
        }

        Long invoiceNo = ServiceFactory.getInvoiceServiceInstance().createInvoice(lineItems, customerNo);

        emptyPhysicalMediumShoppingCart(userId);

        // decrease stock quantities
        for (LineItem lineItem : lineItems) {
            Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();

            medium.setStock(Stock.of(medium.getStock().getQuantity().decreaseBy(lineItem.getQuantity())));
            mediumRepository.update(medium);
        }

        return String.valueOf(invoiceNo);
    }

    public String buyFromDigitalMediumShoppingCart(String userId, String creditCardNo) throws CustomerNotFoundException, ShoppingCartException {
        initializeUserShoppingCartsIfUninitialized(userId);

        if (!ServiceFactory.getCustomerServiceInstance().getCustomerByUsername(userId).getCreditcardNo().equals(creditCardNo)) {
            throw new ShoppingCartException("CreditCardNo does not match!");
        }

        int customerNo = ServiceFactory.getCustomerServiceInstance().getCustomerNoByUsername(userId);

        Set<LineItem> lineItems = digitalMediumLineItems.get(userId);

        if (lineItems.isEmpty()) throw new ShoppingCartException("No LineItems in ShoppingCart");

        Long invoiceNo = ServiceFactory.getInvoiceServiceInstance().createInvoice(lineItems, customerNo);

        emptyDigitalMediumShoppingCart(userId);

        return String.valueOf(invoiceNo);
    }
}
