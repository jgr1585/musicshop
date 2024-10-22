package at.fhv.teamd.musicshop.userclient.view;

import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.exceptions.ShoppingCartException;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.article.ReturnController;
import at.fhv.teamd.musicshop.userclient.view.article.SearchController;
import at.fhv.teamd.musicshop.userclient.view.receive_message.ReceiveMessageController;
import at.fhv.teamd.musicshop.userclient.view.shoppingcart.ShoppingCartController;
import at.fhv.teamd.musicshop.userclient.view.write_message.WriteMessageController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class AppController {

    @FXML
    private FontAwesomeIconView receiveMessageIcon;
    @FXML
    private Tab receiveMessageTab;
    @FXML
    private Tab writeMessageTab;
    @FXML
    private Tab searchTab;
    @FXML
    private Tab returnTab;
    @FXML
    private Tab shoppingCartTab;
    @FXML
    private TabPane tabs;
    @FXML
    private ReceiveMessageController receiveMessageController;
    @FXML
    private WriteMessageController writeMessageController;
    @FXML
    private SearchController searchController;
    @FXML
    private ReturnController returnController;
    @FXML
    private ShoppingCartController shoppingCartController;


    private LoginController loginController;

    @FXML
    public void initialize() {
        this.shoppingCartController.setAppController(this);
        this.receiveMessageController.setAppController(this);

        this.receiveMessageController.bindActiveProperty(receiveMessageTab.selectedProperty());
        this.writeMessageController.bindActiveProperty(writeMessageTab.selectedProperty());
        this.searchController.bindActiveProperty(searchTab.selectedProperty());
        this.returnController.bindActiveProperty(returnTab.selectedProperty());
        this.shoppingCartController.bindActiveProperty(shoppingCartTab.selectedProperty());


        new Thread(() -> {
            this.searchTab.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.SEARCH_ARTICLES_BY_ATTRIBUTES));
            this.returnTab.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.RETURN_ITEM));
            this.shoppingCartTab.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.GET_SHOPPING_CART));
            this.writeMessageTab.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.PUBLISH_MESSAGE));
        }).start();
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void selectSearchTab() {
        tabs.getSelectionModel().select(searchTab);
    }

    public FontAwesomeIconView getReceiveMessageIcon() {
        return this.receiveMessageIcon;
    }

    @FXML
    private void onKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            loginController.logout();
        }
    }

    @FXML
    private void onTabSelectionChanged(Event event) throws IOException, NotAuthorizedException, ShoppingCartException {
        final EventTarget target = event.getTarget();

        if (!(target instanceof Tab))
            return;
        Tab tab = (Tab) target;

        if (tab.equals(shoppingCartTab)) {
            shoppingCartController.reloadShoppingCart();
        }
    }

    @FXML
    private void logout(ActionEvent actionEvent) throws IOException {
        this.loginController.logout();
    }
}
