package at.fhv.teamd.musicshop.userclient.view.shoppingcart;

import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.userclient.Main;
import at.fhv.teamd.musicshop.userclient.Tabs;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.article.ArticleController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public class ShoppingCartController {
    @FXML
    private Label totalAmount;

    @FXML
    private VBox shoppingCardElements;

    @FXML
    private int customerid;

    @FXML
    public void initialize() {
        try {
            reloadShoppingCart();
        } catch (IOException e) {
            clearCart();
        }
    }

    public void reloadShoppingCart() throws IOException {
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
        this.totalAmount.setText(shoppingCartDTO.totalAmount().toString());
        System.out.println("totalAmount: " + shoppingCartDTO.totalAmount().toString());
    }

    @FXML
    private void buyAll(ActionEvent actionEvent) throws IOException {
        if (RemoteFacade.getInstance().buyFromShoppingCart(this.customerid)) {
            new Alert(Alert.AlertType.INFORMATION, "Successfully purchased items", ButtonType.CLOSE).show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Purchase of items failed", ButtonType.CLOSE).show();
        }
        reloadShoppingCart();
        removeCustomer();
    }

    @FXML
    private void removeAll(ActionEvent actionEvent) throws IOException {
        RemoteFacade.getInstance().emptyShoppingCart();
        reloadShoppingCart();
        removeCustomer();
    }

    public void addCustomer(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/customer/customer.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            Stage stage = new Stage();
            stage.setTitle("Select customer");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void removeCustomer(ActionEvent actionEvent) {
        removeCustomer();
    }

    private void removeCustomer() {
        this.customerid = 0;
    }
}
