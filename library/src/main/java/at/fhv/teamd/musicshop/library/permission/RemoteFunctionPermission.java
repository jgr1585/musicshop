package at.fhv.teamd.musicshop.library.permission;

import java.util.Set;

public enum RemoteFunctionPermission {
    searchArticlesByAttributes(Set.of(UserRole.SELLER)),
    searchCustomersByName(searchArticlesByAttributes.getRoles()),
    addToShoppingCart(searchArticlesByAttributes.getRoles()),
    removeFromShoppingCart(searchArticlesByAttributes.getRoles()),
    emptyShoppingCart(searchArticlesByAttributes.getRoles()),
    buyFromShoppingCart(searchArticlesByAttributes.getRoles()),
    getShoppingCart(searchArticlesByAttributes.getRoles()),
    findInvoiceById(searchArticlesByAttributes.getRoles()),
    returnItem(searchArticlesByAttributes.getRoles()),

    publishOrderMessage(Set.of(UserRole.SELLER)),
    publishMessage(Set.of(UserRole.OPERATOR)),
    receiveMessages(Set.of(UserRole.values())),
    acknowledgeMessage(receiveMessages.getRoles()),
    getAllTopics(publishMessage.getRoles());

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
