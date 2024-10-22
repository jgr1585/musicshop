package at.fhv.teamd.musicshop.userclient.view.article;

import at.fhv.teamd.musicshop.library.dto.LineItemDTO;
import at.fhv.teamd.musicshop.library.dto.MediumDTO;
import at.fhv.teamd.musicshop.library.exceptions.InvoiceException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.GenericArticleController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    private LineItemDTO lineItemDTO;

    @FXML
    public void initialize() {
        // force the field to be numeric only
        numberOnly(this.mediumAmount);
        numberOnly(this.mediumAmountSelected);

        new Thread(() -> this.returnButton.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.RETURN_ITEM))).start();
    }

    public void setMediumType(MediumDTO mediumDTO) {
        throw new RuntimeException();
    }

    public void setMediumType(LineItemDTO lineItemDTO) {
        this.lineItemDTO = lineItemDTO;
        this.mediumType.setText(lineItemDTO.medium().type());
        this.mediumAmount.setText(lineItemDTO.quantity().toString());
        this.mediumAmountSelected.setText(String.valueOf(0));
        this.alreadyReturnedAmount.setText(String.valueOf(lineItemDTO.quantityReturn()));
    }

    @FXML
    private void returnItem() throws NotAuthorizedException, InvoiceException {
        RemoteFacade.getInstance().returnItem(lineItemDTO, Integer.parseInt(this.mediumAmountSelected.getText()));
        new Alert(Alert.AlertType.INFORMATION, "Successfully returned items", ButtonType.OK).show();
        this.mediumAmountSelected.setText(Integer.toString(0));
    }

    @FXML
    private void reduceByOne() {
        int val = Integer.parseInt(this.mediumAmountSelected.getText());
        if (val > 0) {
            this.mediumAmountSelected.setText(Integer.toString(val - 1));
        }
    }

    @FXML
    private void increaseByOne() {
        int val = Integer.parseInt(this.mediumAmountSelected.getText());
        if (val < Integer.parseInt(this.mediumAmount.getText()) - Integer.parseInt(this.alreadyReturnedAmount.getText())) {
            this.mediumAmountSelected.setText(Integer.toString(val + 1));
        }
    }
}
