package at.fhv.teamd.musicshop.library.exceptions;

public class AuthenticationFailedException extends Exception {
    public AuthenticationFailedException() {
        super("Authentication failed.");
    }
}
