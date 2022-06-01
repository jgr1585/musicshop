package at.fhv.teamd.musicshop.userclient.observer;

import java.util.LinkedList;
import java.util.List;

public class ShoppingCartSubject {

    static {
        observers = new LinkedList<>();
    }

    private ShoppingCartSubject() {
        throw new IllegalStateException("Utility class");
    }

    private static final List<ShoppingCartObserver> observers;

    public static void addObserver(ShoppingCartObserver observer) {
        observers.add(observer);
    }

    public static void removeObserver(ShoppingCartObserver observer) {
        observers.remove(observer);
    }

    public static void notifyShoppingCartUpdate() {
        observers.forEach(ShoppingCartObserver::updateShoppingCart);
    }
}
