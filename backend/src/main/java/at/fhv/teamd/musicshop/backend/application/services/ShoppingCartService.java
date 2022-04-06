package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.invoice.PaymentMethod;
import at.fhv.teamd.musicshop.backend.domain.person.Customer;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;

import javax.transaction.Transactional;
import java.util.*;

import static at.fhv.teamd.musicshop.backend.application.services.DTOProvider.buildShoppingCartDTO;
import static at.fhv.teamd.musicshop.backend.application.services.InvoiceService.createInvoice;

public class ShoppingCartService {
    private static Map<UUID, Set<LineItem>> sessionLineItems = new HashMap<>();

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
    public void addToShoppingCart(UUID sessionUUID, ArticleDTO articleDTO, MediumDTO mediumDTO, int amount) {
        if (!shoppingCartExists(sessionUUID)) {
            initializeShoppingcart(sessionUUID);
        }
        Set<LineItem> lineItems = sessionLineItems.get(sessionUUID);

        Medium medium = mediumRepository.findMediumById(mediumDTO.id()).orElseThrow();
        Article article = articleRepository.findArticleById(articleDTO.id()).orElseThrow();

        lineItems.stream()
                .filter(li -> li.getMediumId().equals(mediumDTO.id()))
                .findFirst()
                .ifPresentOrElse(li -> {
                    li.increaseQuantity(Quantity.of(amount));
                    System.out.println("amount of lineItem increased");
                }, () -> {
                    lineItems.add(new LineItem(articleDTO.descriptorName(), Quantity.of(amount), medium, article));
                    System.out.println("lineItem added");
                    sessionLineItems.put(sessionUUID, lineItems);
                });
    }

    // TODO: lineItem entfernen bei quantity == 0
    public void removeFromShoppingCart(UUID sessionUUID, MediumDTO mediumDTO, int amount) {
        if (!shoppingCartExists(sessionUUID)) {
            emptyShoppingCart(sessionUUID);
        }

        Set<LineItem> lineItems = sessionLineItems.get(sessionUUID);

        LineItem lineItem = lineItems.stream()
                .filter(li -> li.getMediumId().equals(mediumDTO.id()))
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

    // TODO: overload with customer
    // TODO: append paymentMethod
    // TODO: decrease stock
    public boolean buyFromShoppingCart(UUID sessionUUID, int id) {
        if (!shoppingCartExists(sessionUUID)) {
            emptyShoppingCart(sessionUUID);
        }

        Set<LineItem> lineItems = sessionLineItems.get(sessionUUID);
        lineItems.forEach(lineItem -> {
            Medium medium = mediumRepository.findMediumById(lineItem.getMediumId()).orElseThrow();
            if (medium.getStock().getQuantity().getValue() < lineItem.getQuantity().getValue()) {
                throw  new RuntimeException("not enough in Stock: " + medium.getId());
            }
        });

        try {
//            createInvoice(PaymentMethod.CASH, lineItems, CustomerService.findCustomerById(id));
            createInvoice(PaymentMethod.CASH, lineItems, new Customer("Lukas", "Kaufmann", 100000000));
        } catch (Exception e) {
            e.printStackTrace();
        }

        emptyShoppingCart(sessionUUID);

        // decrease quantities
        lineItems.forEach(lineItem -> {
            Medium medium = mediumRepository.findMediumById(lineItem.getMediumId()).orElseThrow();
            medium.getStock().getQuantity().decreaseBy(lineItem.getQuantity());
        });
        return true;
    }

    private boolean shoppingCartExists(UUID sessionUUID) {
        return sessionLineItems.containsKey(sessionUUID);
    }
}
