package at.fhv.teamd.musicshop.userclient.view.shoppingcart;

import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.userclient.Tabs;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.AppController;
import at.fhv.teamd.musicshop.userclient.view.article.ArticleController;
import at.fhv.teamd.musicshop.userclient.view.customer.CustomerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.naming.ldap.Control;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class ShoppingCartController {
    @FXML
    private Label totalAmount;

    @FXML
    private VBox shoppingCardElements;

    @FXML
    private Label customerNo;

    private AppController appController;

    @FXML
    public void initialize() {
        try {
            reloadShoppingCart();
        } catch (IOException | NotAuthorizedException e) {
            clearCart();
        }
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void reloadShoppingCart() throws IOException, NotAuthorizedException {
        insertData(RemoteFacade.getInstance().getShoppingCart());
    }

    private void clearCart() {
        this.shoppingCardElements.getChildren().clear();
    }

    private void insertData(ShoppingCartDTO shoppingCartDTO) throws IOException {
        clearCart();
        for (var lineItemDTO : shoppingCartDTO.lineItems()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/article.fxml"));
            Parent medium = fxmlLoader.load();
            ArticleController controller = fxmlLoader.getController();
            controller.addMediumTypes(lineItemDTO.article(), lineItemDTO, Tabs.SHOPPINGCART);
            this.shoppingCardElements.getChildren().add(medium);
        }
        DecimalFormat df = new DecimalFormat("0.00");
        this.totalAmount.setText(df.format(shoppingCartDTO.totalAmount()));
        System.out.println("totalAmount: " + df.format(shoppingCartDTO.totalAmount()));
    }

    @FXML
    private void buyAll(ActionEvent actionEvent) throws IOException, NotAuthorizedException {
        int customer = 0;
        if (!customerNo.getText().equals("")) {
            customer = Integer.parseInt(customerNo.getText());
        }
        if (RemoteFacade.getInstance().buyFromShoppingCart(customer)) {
            new Alert(Alert.AlertType.INFORMATION, "Successfully purchased items", ButtonType.CLOSE).show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Purchase of items failed", ButtonType.CLOSE).show();
        }
        reloadShoppingCart();
        removeCustomer();
        appController.selectSearchTab();
    }

    @FXML
    private void removeAll(ActionEvent actionEvent) throws IOException, NotAuthorizedException {
        RemoteFacade.getInstance().emptyShoppingCart();
        reloadShoppingCart();
        removeCustomer();
    }

    public void addCustomer(ActionEvent actionEvent) {
        try {
            AtomicInteger customerNo = new AtomicInteger();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/customer/customer.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            CustomerController controller = fxmlLoader.getController();
            controller.setAtomicInteger(customerNo);
            Stage stage = new Stage();
            stage.setTitle("Select customer");
            stage.setScene(scene);
            stage.showAndWait();
            this.customerNo.setText(customerNo.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void removeCustomer(ActionEvent actionEvent) {
        removeCustomer();
    }

    private void removeCustomer() {
        this.customerNo.setText("");
    }
}
