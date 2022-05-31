package at.fhv.teamd.musicshop.library.exceptions;

public class UnauthorizedInvoiceException extends Exception {
    private UnauthorizedInvoiceException(String message) {
        super(message);
    }

    public static UnauthorizedInvoiceException invoiceAccess() {
        return new UnauthorizedInvoiceException("Not authorized to access this invoice.");
    }

    public static UnauthorizedInvoiceException invoiceNotContainsAlbum() {
        return new UnauthorizedInvoiceException("Invoice does not contain album.");
    }

    public static UnauthorizedInvoiceException invoiceNotContainsSong() {
        return new UnauthorizedInvoiceException("Invoice does not contain song.");
    }
}
