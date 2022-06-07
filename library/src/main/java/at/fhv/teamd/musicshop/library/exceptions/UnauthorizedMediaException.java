package at.fhv.teamd.musicshop.library.exceptions;

public class UnauthorizedMediaException extends Exception {
    public UnauthorizedMediaException() {
        super("Unauthorized media access.");
    }
}
