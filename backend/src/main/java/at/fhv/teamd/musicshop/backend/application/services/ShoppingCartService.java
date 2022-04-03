package at.fhv.teamd.musicshop.backend.application.services;

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

public class ShoppingCartService {
    private static Map<UUID, List<LineItem>> sessionLineItems = new HashMap<>();

    private static MediumRepository mediumRepository;

    ShoppingCartService() {
        mediumRepository = RepositoryFactory.getMediumRepositoryInstance();
    }

    private void initializeShoppingcart(UUID sessionUUID) {
        if (!shoppingCartExists(sessionUUID)) {
            sessionLineItems.put(sessionUUID, new ArrayList<>());
        } else {
            throw new IllegalStateException("Shopping cart already exists.");
        }
    }

    public void addToShoppingCart(UUID sessionUUID, ArticleDTO articleDTO, MediumDTO analogMediumDTO, int amount) {
        if (!shoppingCartExists(sessionUUID)) {
            initializeShoppingcart(sessionUUID);
        }
        List<LineItem> lineItems = sessionLineItems.get(sessionUUID);
        Medium medium = mediumRepository.findMediumById(analogMediumDTO.id()).orElseThrow();

        lineItems.stream()
                .filter(li -> li.getMediumId().equals(analogMediumDTO.id()))
                .findFirst()
                .ifPresentOrElse(li -> {
                    li.increaseQuantity(Quantity.of(amount));
                }, () -> {
                    lineItems.add(new LineItem(articleDTO.descriptorName(), Quantity.of(amount), medium));
                    sessionLineItems.put(sessionUUID, lineItems);
                });
    }

    public void removeFromShoppingCart(UUID sessionUUID, MediumDTO analogMediumDTO, int amount) {
        if (!shoppingCartExists(sessionUUID)) {
            throw new IllegalStateException("Shopping cart does not exist.");
        }

        List<LineItem> lineItems = sessionLineItems.get(sessionUUID);

        lineItems.stream()
                .filter(li -> li.getMediumId().equals(analogMediumDTO.id()))
                .findFirst()
                .orElseThrow()
                .decreaseQuantity(Quantity.of(amount));
    }

    public void emptyShoppingCart(UUID sessionUUID) {
        sessionLineItems.put(sessionUUID, new ArrayList<>());
    }

    public ShoppingCartDTO getShoppingCart(UUID sessionUUID) {
        if (!shoppingCartExists(sessionUUID)) {
            initializeShoppingcart(sessionUUID);
        }
        return buildShoppingCartDTO(mediumRepository, sessionLineItems.get(sessionUUID));
    }

    // TODO: overload with customer
    public void buyFromShoppingCart(UUID sessionUUID, int customerId) {
        if (!shoppingCartExists(sessionUUID)) {
            throw new IllegalStateException("Shopping cart does not exist.");
        }

        // TODO: move lineItems to new invoice

        // clear shoppingCart after purchase
        sessionLineItems.put(sessionUUID, new ArrayList<>());
    }

    private boolean shoppingCartExists(UUID sessionUUID) {
        return sessionLineItems.containsKey(sessionUUID);
    }
}
