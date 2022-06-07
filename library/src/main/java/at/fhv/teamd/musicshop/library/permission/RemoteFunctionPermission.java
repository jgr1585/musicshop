package at.fhv.teamd.musicshop.library.permission;

import java.util.Set;

public enum RemoteFunctionPermission {
    SEARCH_ARTICLES_BY_ATTRIBUTES(Set.of(UserRole.SELLER)),
    SEARCH_CUSTOMERS_BY_NAME(SEARCH_ARTICLES_BY_ATTRIBUTES.getRoles()),
    ADD_TO_SHOPPING_CART(SEARCH_ARTICLES_BY_ATTRIBUTES.getRoles()),
    REMOVE_FROM_SHOPPING_CART(SEARCH_ARTICLES_BY_ATTRIBUTES.getRoles()),
    EMPTY_SHOPPING_CART(SEARCH_ARTICLES_BY_ATTRIBUTES.getRoles()),
    BUY_FROM_SHOPPING_CART(SEARCH_ARTICLES_BY_ATTRIBUTES.getRoles()),
    GET_SHOPPING_CART(SEARCH_ARTICLES_BY_ATTRIBUTES.getRoles()),
    FIND_INVOICE_BY_ID(SEARCH_ARTICLES_BY_ATTRIBUTES.getRoles()),
    RETURN_ITEM(SEARCH_ARTICLES_BY_ATTRIBUTES.getRoles()),

    PUBLISH_ORDER_MESSAGE(Set.of(UserRole.SELLER)),
    PUBLISH_MESSAGE(Set.of(UserRole.OPERATOR)),
    RECEIVE_MESSAGES(Set.of(UserRole.values())),
    ACKNOWLEDGE_MESSAGE(RECEIVE_MESSAGES.getRoles()),
    GET_ALL_TOPICS(PUBLISH_MESSAGE.getRoles());

    private final Set<UserRole> roles;

    RemoteFunctionPermission(Set<UserRole> roles) {
        this.roles = roles;
    }

    public boolean isAllowedForRole(Set<UserRole> roles) {
        if (roles.contains(UserRole.ADMIN)) return true;
        return roles.stream().anyMatch(this.roles::contains);
    }

    private Set<UserRole> getRoles() {
        return this.roles;
    }
}
