package at.fhv.teamd.musicshop.userclient.observer;

import java.util.LinkedList;
import java.util.List;

public class LoginSubject {
    private static final List<LoginObserver> observers;

    static {
        observers = new LinkedList<>();
    }

    public static void addObserver(LoginObserver observer) {
        observers.add(observer);
    }

    public static void removeObserver(LoginObserver observer) {
        observers.remove(observer);
    }

    public static void notifyLogin() {
        observers.forEach(LoginObserver::onLogin);
    }

    public static void notifyLogout() {
        observers.forEach(LoginObserver::onLogout);
    }
}
