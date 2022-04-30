package at.fhv.teamd.musicshop.userclient.view.article;

import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.GenericArticleController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.rmi.RemoteException;

import static at.fhv.teamd.musicshop.userclient.view.FieldValidationHelper.numberOnly;

public class ReturnArticleController implements GenericArticleController {

    @FXML
    private Button returnButton;
    @FXML
    private Label alreadyReturnedAmount;
    @FXML
    private TextField mediumAmount;
    @FXML
    private TextField mediumAmountSelected;
    @FXML
    private Label mediumType;

    private MediumDTO mediumDTO;
    private LineItemDTO lineItemDTO;

    @FXML
    public void initialize() {
        // force the field to be numeric only
        numberOnly(this.mediumAmount);
        numberOnly(this.mediumAmountSelected);

        new Thread(() -> {
            try {
                this.returnButton.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.returnItem));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setMediumType(ArticleDTO articleDTO, MediumDTO mediumDTO) {
        throw new RuntimeException();
    }

    public void setMediumType(LineItemDTO lineItemDTO) {
        this.lineItemDTO = lineItemDTO;
        this.mediumDTO = lineItemDTO.medium();
        this.mediumType.setText(mediumDTO.type());
        this.mediumAmount.setText(lineItemDTO.quantity().toString());
        this.mediumAmountSelected.setText(String.valueOf(0));
        this.alreadyReturnedAmount.setText(String.valueOf(lineItemDTO.quantityReturn()));
    }

    @FXML
    private void returnItem(ActionEvent actionEvent) throws RemoteException, NotAuthorizedException {
        int val = Integer.parseInt(this.mediumAmountSelected.getText());

        if (val > 0 && val <= Integer.parseInt(this.mediumAmount.getText()) - Integer.parseInt(this.alreadyReturnedAmount.getText())) {
            if (RemoteFacade.getInstance().returnItem(lineItemDTO, Integer.parseInt(this.mediumAmountSelected.getText()))) {
                new Alert(Alert.AlertType.INFORMATION, "Successfully returned items", ButtonType.OK).show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Item could not be returned", ButtonType.CLOSE).show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Check quantity to return", ButtonType.CLOSE).show();
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
        if (val < Integer.parseInt(this.mediumAmount.getText()) - Integer.parseInt(this.alreadyReturnedAmount.getText())) {
            this.mediumAmountSelected.setText(Integer.valueOf(val + 1).toString());
        }
    }
}
