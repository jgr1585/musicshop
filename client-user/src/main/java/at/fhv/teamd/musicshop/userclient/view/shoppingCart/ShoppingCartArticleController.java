package at.fhv.teamd.musicshop.userclient.view.shoppingCart;

import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.generic.GenericArticleController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.util.Optional;

import static at.fhv.teamd.musicshop.userclient.view.FieldValidationHelper.numberOnly;

public class ShoppingCartArticleController implements GenericArticleController {

    @FXML
    private Label mediumType;
    @FXML
    private TextField mediumAmount;
    @FXML
    private Label mediumPrice;

    private ArticleDTO articleDTO;
    private MediumDTO analogMediumDTO;
    private LineItemDTO lineItemDTO;

    @FXML
    public void initialize() {
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
        RemoteFacade.getInstance().addToShoppingCart(articleDTO, analogMediumDTO, 1);
    }
    @FXML
    private void increaseByOne(ActionEvent actionEvent) throws RemoteException {
        RemoteFacade.getInstance().removeFromShoppingCart(analogMediumDTO, 1);
    }
    @FXML
    private void remove(ActionEvent actionEvent) throws RemoteException {
        RemoteFacade.getInstance().removeFromShoppingCart(analogMediumDTO, lineItemDTO.quantity());
    }
}
