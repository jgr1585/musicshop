package at.fhv.teamd.musicshop.userclient.view.article;

import at.fhv.teamd.musicshop.library.dto.LineItemDTO;
import at.fhv.teamd.musicshop.library.dto.MediumDTO;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.exceptions.ShoppingCartException;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.GenericArticleController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static at.fhv.teamd.musicshop.userclient.view.FieldValidationHelper.numberOnly;

public class SearchArticleController implements GenericArticleController {

    @FXML
    private Button addToCartButton;
    @FXML
    private Button orderButton;
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
        numberOnly(this.mediumAmountSelected);

        new Thread(() -> {
            this.addToCartButton.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.ADD_TO_SHOPPING_CART));
            this.orderButton.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.PUBLISH_ORDER_MESSAGE));
        }).start();
    }

    public void setMediumType(MediumDTO mediumDTO) {
        this.mediumDTO = mediumDTO;
        this.mediumType.setText(mediumDTO.type());
        this.mediumPrice.setText(mediumDTO.price().toString());
        this.mediumAmount.setText(mediumDTO.stockQuantity().toString());
        this.mediumAmountSelected.setText(Integer.toString(0));
    }

    public void setMediumType(LineItemDTO lineItemDTO) {
        this.mediumDTO = lineItemDTO.medium();
        this.mediumType.setText(mediumDTO.type());
        this.mediumPrice.setText(mediumDTO.price().toString());
        this.mediumAmount.setText(mediumDTO.stockQuantity().toString());
        this.mediumAmountSelected.setText(lineItemDTO.quantity().toString());
    }

    @FXML
    private void addToCart() throws NotAuthorizedException, ShoppingCartException {
        RemoteFacade.getInstance().addToShoppingCart(this.mediumDTO, Integer.parseInt(this.mediumAmountSelected.getText()));
        new Alert(Alert.AlertType.INFORMATION, "Successfully added items", ButtonType.OK).show();
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
        this.mediumAmountSelected.setText(Integer.toString(val + 1));
    }

    @FXML
    private void order() throws NotAuthorizedException, MessagingException {
        RemoteFacade.getInstance().publishOrderMessage(mediumDTO, mediumAmountSelected.getText());
        new Alert(Alert.AlertType.INFORMATION, "Message successfully sent", ButtonType.CLOSE).show();
        this.mediumAmountSelected.setText(Integer.toString(0));
    }
}
