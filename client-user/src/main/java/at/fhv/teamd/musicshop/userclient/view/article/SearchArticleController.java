package at.fhv.teamd.musicshop.userclient.view.article;

import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.exceptions.ShoppingCartException;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.GenericArticleController;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

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
            this.addToCartButton.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.addToShoppingCart));
            this.orderButton.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.publishOrderMessage));
        }).start();
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
    private void addToCart(ActionEvent actionEvent) throws NotAuthorizedException, ShoppingCartException {
        int amount = Integer.parseInt(this.mediumAmountSelected.getText());
        RemoteFacade.getInstance().addToShoppingCart(this.mediumDTO, amount);

        Label lbl = mediumPrice;
        Paint paintBefore = lbl.getTextFill();
        String textBefore = lbl.getText();
        Timeline flashInfo = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    lbl.setTextFill(Color.RED);
                    lbl.setText("ADDED!");
                }),

                new KeyFrame(Duration.millis(4000), e -> {
                    lbl.setTextFill(paintBefore);
                    lbl.setText(textBefore);
                })
        );
        flashInfo.play();
    }

    public void reduceByOne(ActionEvent actionEvent) {
        int val = Integer.parseInt(this.mediumAmountSelected.getText());
        if (val > 0)
            this.mediumAmountSelected.setText(Integer.valueOf(val - 1).toString());
    }

    public void increaseByOne(ActionEvent actionEvent) {
        int val = Integer.parseInt(this.mediumAmountSelected.getText());
        int valInStock = Integer.parseInt(this.mediumAmount.getText());
        if (val < valInStock)
            this.mediumAmountSelected.setText(Integer.valueOf(val + 1).toString());
    }

    public void order(ActionEvent actionEvent) throws NotAuthorizedException, MessagingException {
        RemoteFacade.getInstance().publishOrderMessage(mediumDTO, mediumAmountSelected.getText());
        new Alert(Alert.AlertType.INFORMATION, "Message successfully sent", ButtonType.CLOSE).show();
        this.mediumAmountSelected.setText(Integer.valueOf(0).toString());
    }
}
