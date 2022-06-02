package at.fhv.teamd.musicshop.backend.application.services;

public class UnauthorizedMediaException extends Exception {
    public UnauthorizedMediaException() {
        super("Unauthorized media access.");
    }
}
