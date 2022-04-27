package at.fhv.teamd.musicshop.userclient.view;

import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.shoppingcart.ShoppingCartController;
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
    private Tab returnTab;
    @FXML
    private Tab shoppingCartTab;
    @FXML
    private Tab newMessageTab;
    @FXML
    private Tab inboxTab;
    @FXML
    private TabPane tabs;
    @FXML
    private Tab searchTab;
    @FXML
    private ShoppingCartController shoppingCartController;

    private LoginController loginController;

    @FXML
    public void initialize() {
        shoppingCartController.setAppController(this);

        new Thread(() -> {
            try {
                this.shoppingCartTab.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.getShoppingCart));
                this.newMessageTab.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.publishMessage));
                this.inboxTab.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.receiveMessages));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void selectSearchTab() {
        tabs.getSelectionModel().select(searchTab);
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
