package at.fhv.teamd.musicshop.library.exceptions;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException() {
        super("Customer not found.");
    }
}
