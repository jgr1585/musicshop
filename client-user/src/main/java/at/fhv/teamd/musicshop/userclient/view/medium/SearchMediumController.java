package at.fhv.teamd.musicshop.userclient.view.medium;

import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.GenericArticleController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;

import static at.fhv.teamd.musicshop.userclient.view.FieldValidationHelper.numberOnly;

public class SearchMediumController implements GenericArticleController {

    @FXML
    private Label mediumPrice;
    @FXML
    private TextField mediumAmount;
    @FXML
    private TextField mediumAmountSelected;
    @FXML
    private Label mediumType;

    private MediumDTO mediumDTO;

    @FXML
    public void initialize() {
        // force the field to be numeric only
        numberOnly(this.mediumAmount);
    }

    public void setMediumType(ArticleDTO articleDTO, MediumDTO mediumDTO) {
        this.mediumDTO = mediumDTO;
        this.mediumType.setText(mediumDTO.type());
        this.mediumPrice.setText(mediumDTO.price().toString());
        this.mediumAmount.setText(mediumDTO.stockQuantity().toString());
        this.mediumAmountSelected.setText(Integer.valueOf(0).toString());
    }

    public void setMediumType(LineItemDTO lineItemDTO) {
        this.mediumDTO = lineItemDTO.medium();
        this.mediumType.setText(mediumDTO.type());
        this.mediumPrice.setText(mediumDTO.price().toString());
        this.mediumAmount.setText(mediumDTO.stockQuantity().toString());
        this.mediumAmountSelected.setText(lineItemDTO.quantity().toString());
    }

    @FXML
    private void addToCart(ActionEvent actionEvent) throws RemoteException, NotAuthorizedException {
        if (RemoteFacade.getInstance().addToShoppingCart(this.mediumDTO, Integer.parseInt(this.mediumAmountSelected.getText()))) {
            new Alert(Alert.AlertType.INFORMATION, "Successfully added items", ButtonType.OK).show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Item could not be added", ButtonType.CLOSE).show();
        }
        this.mediumAmountSelected.setText(Integer.valueOf(0).toString());
    }

    public void reduceByOne(ActionEvent actionEvent) {
        int val = Integer.parseInt(this.mediumAmountSelected.getText());
        if (val > 0) {
            this.mediumAmountSelected.setText(Integer.valueOf(val - 1).toString());
        }
    }

    public void increaseByOne(ActionEvent actionEvent) {
        int val = Integer.parseInt(this.mediumAmountSelected.getText());
        this.mediumAmountSelected.setText(Integer.valueOf(val + 1).toString());
    }

    public void order(ActionEvent actionEvent) throws RemoteException, NotAuthorizedException {
        if (RemoteFacade.getInstance().publishOrder(mediumDTO, mediumAmountSelected.getText())) {
            new Alert(Alert.AlertType.INFORMATION, "Message successfully sent", ButtonType.CLOSE).show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Send message failed", ButtonType.CLOSE).show();
        }
        this.mediumAmountSelected.setText(Integer.valueOf(0).toString());
    }
}
