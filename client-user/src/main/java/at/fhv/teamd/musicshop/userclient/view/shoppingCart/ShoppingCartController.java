package at.fhv.teamd.musicshop.userclient.view.shoppingCart;

import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.userclient.Tabs;
import at.fhv.teamd.musicshop.userclient.view.ArticleController;
import at.fhv.teamd.musicshop.userclient.connection.RMIconnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ShoppingCartController implements Initializable {
    @FXML
    private VBox shoppingCardElements;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.reloadShoppingCard();
    }

    public void reloadShoppingCard() {
        ApplicationClient client = RMIconnection.getApplicationClient();
        try {
            insertData(client.getShoppingCart());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertData(ShoppingCartDTO shoppingCartDTO) {
        this.shoppingCardElements.getChildren().clear();

        shoppingCartDTO.lineItems().forEach(lineItemDTO -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/view/article.fxml"));
                Parent medium = fxmlLoader.load();
                ArticleController controller = fxmlLoader.getController();
                controller.addMediumTypes(lineItemDTO.article(), Optional.of(lineItemDTO), Tabs.SHOPPINGCART);
                this.shoppingCardElements.getChildren().add(medium);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void buyAll(ActionEvent actionEvent) throws RemoteException {
        //TODO: Check and buy

        ApplicationClient client = RMIconnection.getApplicationClient();
        client.emptyShoppingCart();

        new Alert(Alert.AlertType.INFORMATION, "Successfully purchased items", ButtonType.CLOSE).show();

        this.reloadShoppingCard();
    }

}
