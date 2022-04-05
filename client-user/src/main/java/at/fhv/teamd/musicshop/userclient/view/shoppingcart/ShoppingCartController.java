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
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Optional;

public class ShoppingCartController {
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
    }

    @FXML
    private void buyAll(ActionEvent actionEvent) throws IOException {
        //TODO: Check and buy

        RemoteFacade.getInstance().buyFromShoppingCart(0);

        new Alert(Alert.AlertType.INFORMATION, "Successfully purchased items", ButtonType.CLOSE).show();

        this.reloadShoppingCart();
    }

}
