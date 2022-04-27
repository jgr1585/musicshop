package at.fhv.teamd.musicshop.library.permission;

import java.util.Set;

public enum RemoteFunctionPermission {
    searchArticlesByAttributes(Set.of(UserRole.values())),
    searchCustomersByName(Set.of(UserRole.values())),
    addToShoppingCart(Set.of(UserRole.values())),
    removeFromShoppingCart(Set.of(UserRole.values())),
    emptyShoppingCart(Set.of(UserRole.values())),
    buyFromShoppingCart(Set.of(UserRole.values())),
    getShoppingCart(Set.of(UserRole.values())),
    publishOrderMessage(Set.of(UserRole.values())),
    publishMessage(Set.of(UserRole.OPERATOR, UserRole.ADMIN)),
    receiveMessages(Set.of(UserRole.values())),
    acknowledgeMessage(Set.of(UserRole.values())),
    getAllTopics(Set.of(UserRole.values()));

    private final Set<UserRole> roles;

    RemoteFunctionPermission(Set<UserRole> roles) {
        this.roles = roles;
    }

    public boolean isAllowedForRole(UserRole role) {
        return roles.contains(role);
    }
}
