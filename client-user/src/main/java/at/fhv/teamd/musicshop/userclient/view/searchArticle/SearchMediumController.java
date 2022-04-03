package at.fhv.teamd.musicshop.userclient.view.searchArticle;

import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.generic.GenericArticleController;
import at.fhv.teamd.musicshop.userclient.view.generic.GenericArticleFunctions;
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
    private Label mediumType;

    private ArticleDTO articleDTO;
    private MediumDTO analogMediumDTO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // force the field to be numeric only
        numberOnly(this.mediumAmount);
    }



    public void setMediumType(ArticleDTO articleDTO, MediumDTO analogMedium, Optional<LineItemDTO> lineItemDTO) {
        this.articleDTO = articleDTO;
        this.analogMediumDTO = analogMedium;

        this.mediumType.setText(analogMedium.type());
        this.mediumPrice.setText(analogMedium.price().toString());
    }

    @FXML
    private void addToCard(ActionEvent actionEvent) throws RemoteException {
        RemoteFacade.getInstance().addToShoppingCart(this.articleDTO, this.analogMediumDTO, Integer.parseInt(this.mediumAmount.getText()));
    }
}
