package at.fhv.teamd.musicshop.userclient.view.shoppingcart;

import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.observer.ShoppingCartSubject;
import at.fhv.teamd.musicshop.userclient.view.GenericArticleController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;

import static at.fhv.teamd.musicshop.userclient.view.FieldValidationHelper.numberOnly;

public class ShoppingCartArticleController implements GenericArticleController {

    @FXML
    public TextField mediumAmountSelected;
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
    }

    @Override
    public void setMediumType(ArticleDTO articleDTO, MediumDTO mediumDTO) {
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
    private void reduceByOne(ActionEvent actionEvent) throws RemoteException, NotAuthorizedException {
        int val = Integer.parseInt(this.mediumAmountSelected.getText());
        if (val > 0) {
            this.mediumAmountSelected.setText(Integer.valueOf(val - 1).toString());
            if (!RemoteFacade.getInstance().removeFromShoppingCart(mediumDTO, 1)) {
                new Alert(Alert.AlertType.ERROR, "Error on remove from shoppingcart", ButtonType.CLOSE).show();
            }
        }
    }
    @FXML
    private void increaseByOne(ActionEvent actionEvent) throws RemoteException, NotAuthorizedException {
        int val = Integer.parseInt(this.mediumAmountSelected.getText());
        if (val < this.lineItemDTO.medium().stockQuantity()) {
            this.mediumAmountSelected.setText(Integer.valueOf(val + 1).toString());
            if (!RemoteFacade.getInstance().addToShoppingCart(mediumDTO, 1)) {
                new Alert(Alert.AlertType.ERROR, "Error on add from shoppingcart", ButtonType.CLOSE).show();
            }
        }
    }
    @FXML
    private void remove(ActionEvent actionEvent) throws RemoteException, NotAuthorizedException {
        RemoteFacade.getInstance().removeFromShoppingCart(mediumDTO, lineItemDTO.quantity());
        ShoppingCartSubject.notifyShoppingCartUpdate();
    }
}
