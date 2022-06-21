package at.fhv.teamd.musicshop.userclient.view.shoppingcart;

import at.fhv.teamd.musicshop.library.dto.LineItemDTO;
import at.fhv.teamd.musicshop.library.dto.MediumDTO;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.exceptions.ShoppingCartException;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.GenericArticleController;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import static at.fhv.teamd.musicshop.userclient.view.FieldValidationHelper.numberOnly;

public class ShoppingCartArticleController implements GenericArticleController {

    @FXML
    private TextField mediumAmountSelected;
    @FXML
    private Button removeButton;
    @FXML
    private Label mediumType;
    @FXML
    private TextField mediumAmountStock;
    @FXML
    private Label mediumPrice;

    private MediumDTO mediumDTO;
    private LineItemDTO lineItemDTO;

    @FXML
    public void initialize() {
        // force the field to be numeric only
        numberOnly(this.mediumAmountStock);

        new Thread(() -> this.removeButton.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.REMOVE_FROM_SHOPPING_CART))).start();
    }

    @Override
    public void setMediumType(MediumDTO mediumDTO) {
        throw new RuntimeException();
    }

    @Override
    public void setMediumType(LineItemDTO lineItemDTO) {
        this.mediumDTO = lineItemDTO.medium();
        this.lineItemDTO = lineItemDTO;
        this.mediumType.setText(mediumDTO.type());
        this.mediumPrice.setText(mediumDTO.price().toString());
        this.mediumAmountStock.setText(this.lineItemDTO.medium().stockQuantity().toString());
        this.mediumAmountSelected.setText(this.lineItemDTO.quantity().toString());
    }

    @FXML
    private void reduceByOne() throws NotAuthorizedException, ShoppingCartException {
        int val = Integer.parseInt(this.mediumAmountSelected.getText());
        if (val > 0) {
            this.mediumAmountSelected.setText(Integer.toString(val - 1));
            RemoteFacade.getInstance().removeFromShoppingCart(mediumDTO, 1);
        }
    }

    @FXML
    private void increaseByOne() throws NotAuthorizedException, ShoppingCartException {
        int val = Integer.parseInt(this.mediumAmountSelected.getText());
        if (val < this.lineItemDTO.medium().stockQuantity()) {
            this.mediumAmountSelected.setText(Integer.toString(val + 1));
            RemoteFacade.getInstance().addToShoppingCart(mediumDTO, 1);
        }
    }

    @FXML
    private void remove() throws NotAuthorizedException, ShoppingCartException {
        RemoteFacade.getInstance().removeFromShoppingCart(mediumDTO, lineItemDTO.quantity());
    }
}
