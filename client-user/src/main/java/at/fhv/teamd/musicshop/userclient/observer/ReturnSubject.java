package at.fhv.teamd.musicshop.userclient.observer;

import java.util.LinkedList;
import java.util.List;

public class ReturnSubject {

    static {
        observers = new LinkedList<>();
    }

    private static final List<ReturnObserver> observers;

    public static void addObserver(ReturnObserver observer) {
        observers.add(observer);
    }

    public static void removeObserver(ReturnObserver observer) {
        observers.remove(observer);
    }

    public static void notifyReturnUpdate() {
        observers.forEach(ReturnObserver::updateReturn);
    }
}
