package at.fhv.teamd.musicshop.userclient.view.shoppingcart;

import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.userclient.Tabs;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.article.ArticleController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public class ShoppingCartController {
    @FXML
    private Label totalAmount;

    @FXML
    private VBox shoppingCardElements;

    // TODO: fix initialize shopping cart;

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
        if (RemoteFacade.getInstance().buyFromShoppingCart(0)) {
            new Alert(Alert.AlertType.INFORMATION, "Successfully purchased items", ButtonType.CLOSE).show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Purchase of items failed", ButtonType.CLOSE).show();
        }
        this.reloadShoppingCart();
    }

    @FXML
    private void removeAll(ActionEvent actionEvent) throws IOException {
        RemoteFacade.getInstance().emptyShoppingCart();
    }

}
