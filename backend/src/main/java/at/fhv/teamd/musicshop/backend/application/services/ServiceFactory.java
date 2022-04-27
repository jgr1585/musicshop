package at.fhv.teamd.musicshop.backend.application.services;

public class ServiceFactory {
    private static AuthService authService;
    private static ArticleService articleService;
    private static CustomerService customerService;
    private static InvoiceService invoiceService;
    private static MessageService messageService;
    private static ShoppingCartService shoppingCartService;

    private ServiceFactory() {}

    public static ArticleService getArticleServiceInstance() {
        if (articleService == null) {
            articleService = new ArticleService();
        }
        return articleService;
    }

    public static ShoppingCartService getShoppingCartServiceInstance() {
        if (shoppingCartService == null) {
            shoppingCartService = new ShoppingCartService();
        }
        return shoppingCartService;
    }

    public static InvoiceService getInvoiceServiceInstance() {
        if (invoiceService == null) {
            invoiceService = new InvoiceService();
        }
        return invoiceService;
    }

    public static CustomerService getCustomerServiceInstance() {
        if (customerService == null) {
            customerService = new CustomerService();
        }
        return customerService;
    }

    public static AuthService getAuthServiceInstance() {
        if (authService == null) {
            authService = new AuthService();
        }
        return authService;
    }

    public static MessageService getMessageServiceInstance() {
        if (messageService == null) {
            messageService = new MessageService();
        }
        return messageService;
    }
}
