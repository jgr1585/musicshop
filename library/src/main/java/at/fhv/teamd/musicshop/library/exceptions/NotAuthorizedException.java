package at.fhv.teamd.musicshop.library.exceptions;

public class NotAuthorizedException extends Exception {
    public NotAuthorizedException() {
        super("Authorization level too low to perform this action.");
    }
}
