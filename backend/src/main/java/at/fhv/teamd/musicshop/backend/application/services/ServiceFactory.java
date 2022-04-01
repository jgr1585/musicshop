package at.fhv.teamd.musicshop.backend.application.services;

public class ServiceFactory {
    private static ArticleService articleService;
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
}
