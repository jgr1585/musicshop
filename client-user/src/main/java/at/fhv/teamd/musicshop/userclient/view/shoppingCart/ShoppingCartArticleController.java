package at.fhv.teamd.musicshop.userclient.view.shoppingCart;

import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.userclient.view.generic.GenericArticleController;
import at.fhv.teamd.musicshop.userclient.view.generic.GenericArticleFunctions;
import at.fhv.teamd.musicshop.userclient.connection.RMIconnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ShoppingCartArticleController extends GenericArticleFunctions implements GenericArticleController, Initializable {

    @FXML
    private Label mediumType;
    @FXML
    private TextField mediumAmount;
    @FXML
    private Label mediumPrice;

    private ArticleDTO articleDTO;
    private MediumDTO analogMediumDTO;
    private LineItemDTO lineItemDTO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // force the field to be numeric only
        numberOnly(this.mediumAmount);
    }

    @Override
    public void setMediumType(ArticleDTO articleDTO, MediumDTO analogMedium, Optional<LineItemDTO> lineItemDTO) {
        this.articleDTO = articleDTO;
        this.analogMediumDTO = analogMedium;
        this.lineItemDTO = lineItemDTO.orElseThrow();

        this.mediumType.setText(analogMedium.type());
        this.mediumPrice.setText(analogMedium.price().toString());
        this.mediumAmount.setText(this.lineItemDTO.quantity().toString());
    }

    @FXML
    private void reduceByOne(ActionEvent actionEvent) throws RemoteException {
        ApplicationClient client = RMIconnection.getApplicationClient();
        client.addToShoppingCart(articleDTO, analogMediumDTO, 1);
    }
    @FXML
    private void increaseByOne(ActionEvent actionEvent) throws RemoteException {
        ApplicationClient client = RMIconnection.getApplicationClient();
        client.removeFromShoppingCart(analogMediumDTO, 1);
    }
    @FXML
    private void remove(ActionEvent actionEvent) throws RemoteException {
        ApplicationClient client = RMIconnection.getApplicationClient();
        client.removeFromShoppingCart(analogMediumDTO, lineItemDTO.quantity());
    }
}
