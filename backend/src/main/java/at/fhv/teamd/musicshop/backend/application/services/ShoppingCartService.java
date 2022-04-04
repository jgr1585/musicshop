package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.invoice.PaymentMethod;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;

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

    public void removeFromShoppingCart(UUID sessionUUID, MediumDTO mediumDTO, int amount) {
        if (!shoppingCartExists(sessionUUID)) {
            throw new IllegalStateException("Shopping cart does not exist.");
        }

        Set<LineItem> lineItems = sessionLineItems.get(sessionUUID);

        lineItems.stream()
                .filter(li -> li.getMediumId().equals(mediumDTO.id()))
                .findFirst()
                .orElseThrow()
                .decreaseQuantity(Quantity.of(amount));
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
    public void buyFromShoppingCart(UUID sessionUUID, int customerId) {
        if (!shoppingCartExists(sessionUUID)) {
            throw new IllegalStateException("Shopping cart does not exist.");
        }

        Set<LineItem> lineItems = sessionLineItems.get(sessionUUID);
        lineItems.forEach(lineItem -> {
            Medium medium = mediumRepository.findMediumById(lineItem.getMediumId()).orElseThrow();
            if (medium.getStock().getQuantity().getValue() < lineItem.getQuantity().getValue()) {
                throw  new RuntimeException("not enough in Stock: " + medium.getId());
            }
        });

        createInvoice(PaymentMethod.CASH, lineItems, null, null);
        sessionLineItems.put(sessionUUID, new HashSet<>());
    }

    public void buyFromShoppingCart(UUID sessionUUID) {
        if (!shoppingCartExists(sessionUUID)) {
            throw new IllegalStateException("Shopping cart does not exist.");
        }

        Set<LineItem> lineItems = sessionLineItems.get(sessionUUID);
        lineItems.forEach(lineItem -> {
            Medium medium = mediumRepository.findMediumById(lineItem.getMediumId()).orElseThrow();
            if (medium.getStock().getQuantity().getValue() < lineItem.getQuantity().getValue()) {
                throw  new RuntimeException("not enough in Stock: " + medium.getId());
            }
        });

        createInvoice(PaymentMethod.CASH, lineItems, null);
        sessionLineItems.put(sessionUUID, new HashSet<>());
    }

    private boolean shoppingCartExists(UUID sessionUUID) {
        return sessionLineItems.containsKey(sessionUUID);
    }
}
