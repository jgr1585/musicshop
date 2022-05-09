package at.fhv.teamd.musicshop.userclient.view;

import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.receiveMessage.ReceiveMessageController;
import at.fhv.teamd.musicshop.userclient.view.shoppingcart.ShoppingCartController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.rmi.RemoteException;

public class AppController {

    @FXML
    private FontAwesomeIconView receiveMessageIcon;
    @FXML
    private Tab searchTab;
    @FXML
    private Tab returnTab;
    @FXML
    private Tab shoppingCartTab;
    @FXML
    private Tab newMessageTab;
    @FXML
    private TabPane tabs;
    @FXML
    private ShoppingCartController shoppingCartController;
    @FXML
    private ReceiveMessageController receiveMessageController;

    private LoginController loginController;

    @FXML
    public void initialize() {
        shoppingCartController.setAppController(this);
        this.receiveMessageController.setAppController(this);

        new Thread(() -> {
            this.searchTab.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.searchArticlesByAttributes));
            this.returnTab.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.returnItem));
            this.shoppingCartTab.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.getShoppingCart));
            this.newMessageTab.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.publishMessage));
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
    public void loadOnSelection(Event event) throws IOException, NotAuthorizedException {
        shoppingCartController.reloadShoppingCart();
    }

    @FXML
    private void logout(ActionEvent actionEvent) throws IOException {
        this.loginController.logout();
    }
}
