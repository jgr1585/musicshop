package at.fhv.teamd.musicshop.userclient.view.medium;

import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.GenericArticleController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;

import static at.fhv.teamd.musicshop.userclient.view.FieldValidationHelper.numberOnly;

public class SearchMediumController implements GenericArticleController {

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

    @FXML
    public void initialize() {
        // force the field to be numeric only
        numberOnly(this.mediumAmount);
    }

    public void setMediumType(ArticleDTO articleDTO, MediumDTO mediumDTO) {
        this.articleDTO = articleDTO;
        this.mediumDTO = mediumDTO;
        this.mediumType.setText(mediumDTO.type());
        this.mediumPrice.setText(mediumDTO.price().toString());
        this.mediumAmount.setText(mediumDTO.stockQuantity().toString());
        this.mediumAmountSelected.setText(Integer.valueOf(0).toString());
    }

    public void setMediumType(ArticleDTO articleDTO, MediumDTO mediumDTO, LineItemDTO lineItemDTO) {
        setMediumType(articleDTO, mediumDTO);
    }

    @FXML
    private void addToCard(ActionEvent actionEvent) throws RemoteException {
        RemoteFacade.getInstance().addToShoppingCart(this.articleDTO, this.mediumDTO, Integer.parseInt(this.mediumAmount.getText()));
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