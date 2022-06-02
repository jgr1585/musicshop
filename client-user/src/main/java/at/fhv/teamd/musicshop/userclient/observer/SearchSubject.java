package at.fhv.teamd.musicshop.userclient.observer;

import java.util.LinkedList;
import java.util.List;

public class SearchSubject {

    private static final List<SearchObserver> observers = new LinkedList<>();

    private SearchSubject() {
        throw new IllegalStateException("Utility class");
    }

    public static void addObserver(SearchObserver observer) {
        observers.add(observer);
    }

    public static void removeObserver(SearchObserver observer) {
        observers.remove(observer);
    }

    public static void notifyObserversUpdate() {
        observers.forEach(SearchObserver::updateSearch);
    }
}
