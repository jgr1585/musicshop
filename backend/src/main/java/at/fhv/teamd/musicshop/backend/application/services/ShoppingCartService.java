package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.library.DTO.AnalogMediumDTO;
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

    public void addToShoppingCart(UUID sessionUUID, ArticleDTO articleDTO, AnalogMediumDTO analogMediumDTO, int amount) {
        if (!sessionLineItems.containsKey(sessionUUID)) {
            sessionLineItems.put(sessionUUID, new ArrayList<>());
        }
        List<LineItem> lineItems = sessionLineItems.get(sessionUUID);
        Medium medium = mediumRepository.findAnalogMediumById(analogMediumDTO.id()).orElseThrow();

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

    public void removeFromShoppingCart(UUID sessionUUID, AnalogMediumDTO analogMediumDTO, int amount) {
        if (!sessionLineItems.containsKey(sessionUUID)) {
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
        if (sessionLineItems.containsKey(sessionUUID)) {
            sessionLineItems.put(sessionUUID, new ArrayList<>());
        }
    }

    public ShoppingCartDTO getShoppingCart(UUID sessionUUID) {
        if (!sessionLineItems.containsKey(sessionUUID)) {
            throw new IllegalStateException("Shopping cart does not exist.");
        }

        return buildShoppingCartDTO(mediumRepository, sessionLineItems.get(sessionUUID));
    }
}
