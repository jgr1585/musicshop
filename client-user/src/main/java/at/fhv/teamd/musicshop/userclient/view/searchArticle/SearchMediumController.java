package at.fhv.teamd.musicshop.userclient.view.searchArticle;

import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.userclient.connection.RMIconnection;
import at.fhv.teamd.musicshop.userclient.view.generic.GenericArticleController;
import at.fhv.teamd.musicshop.userclient.view.generic.GenericArticleFunctions;
import at.fhv.teamd.musicshop.userclient.view.shoppingCart.ShoppingCartController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;

public class SearchMediumController extends GenericArticleFunctions implements GenericArticleController, Initializable {

    @FXML
    private Label title;
    @FXML
    private Label artist;
    @FXML
    private Label genre;
    @FXML
    private VBox mediumTypeList;

    @FXML
    private Label mediumPrice;
    @FXML
    private TextField mediumAmount;
    @FXML
    private TextField mediumAmountSelected;
    @FXML
    private Label mediumType;

    private ArticleDTO articleDTO;
    private MediumDTO mediumDTO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // force the field to be numeric only
        numberOnly(this.mediumAmount);
    }

    public void setMediumType(ArticleDTO articleDTO, MediumDTO mediumDTO, Optional<LineItemDTO> lineItemDTO) {
        this.articleDTO = articleDTO;
        this.mediumDTO = mediumDTO;
        this.mediumType.setText(mediumDTO.type());
        this.mediumPrice.setText(mediumDTO.price().toString());
        this.mediumAmount.setText(mediumDTO.stockQuantity().toString());
        this.mediumAmountSelected.setText(Integer.valueOf(0).toString());
    }

    @FXML
    private void addToCard(ActionEvent actionEvent) throws RemoteException {
        ApplicationClient client = RMIconnection.getApplicationClient();
        client.addToShoppingCart(this.articleDTO, this.mediumDTO, Integer.parseInt(this.mediumAmount.getText()));
    }

    public void reduceByOne(ActionEvent actionEvent) {
        int val = Integer.parseInt(this.mediumAmountSelected.getText());
        if (val >= 1) {
            this.mediumAmountSelected.setText(Integer.valueOf(val - 1).toString());
        }
    }

    public void increaseByOne(ActionEvent actionEvent) {
        int val = Integer.parseInt(this.mediumAmountSelected.getText());
        if (val >= 0) {
            this.mediumAmountSelected.setText(Integer.valueOf(val + 1).toString());
        }
    }
}
