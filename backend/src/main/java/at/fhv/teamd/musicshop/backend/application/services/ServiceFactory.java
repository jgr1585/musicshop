package at.fhv.teamd.musicshop.backend.application.services;

import java.util.logging.Handler;

public class ServiceFactory {
    private static ArticleService articleService;
    private static ShoppingCartService shoppingCartService;
    private static InvoiceService invoiceService;
    private static CustomerService customerService;
    private static AuthService authService;
    private static MessageService messageService;

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
