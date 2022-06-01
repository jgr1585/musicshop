package at.fhv.teamd.musicshop.userclient.view.shoppingcart;

import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.exceptions.ShoppingCartException;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.Tabs;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.observer.ShoppingCartObserver;
import at.fhv.teamd.musicshop.userclient.observer.ShoppingCartSubject;
import at.fhv.teamd.musicshop.userclient.view.ActivePropertyBindable;
import at.fhv.teamd.musicshop.userclient.view.AppController;
import at.fhv.teamd.musicshop.userclient.view.article.ArticleController;
import at.fhv.teamd.musicshop.userclient.view.customer.CustomerController;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ShoppingCartController implements ShoppingCartObserver, ActivePropertyBindable {

    @FXML
    private Button removeButton;
    @FXML
    private Button selectButton;
    @FXML
    private Button formCancelBtn;
    @FXML
    private Button formSubmitBtn;
    @FXML
    private Label totalAmount;
    @FXML
    private VBox shoppingCartElements;
    @FXML
    private Label customerNo;

    private AppController appController;

    public void bindActiveProperty(ReadOnlyBooleanProperty activeProp) {
        formCancelBtn.cancelButtonProperty().bind(activeProp);
        formSubmitBtn.defaultButtonProperty().bind(activeProp);
    }

    @FXML
    public void initialize() {
        try {
            reloadShoppingCart();
        } catch (IOException | NotAuthorizedException | ShoppingCartException e) {
            clearCart();
        }

        ShoppingCartSubject.addObserver(this);

        new Thread(() -> {
            this.formCancelBtn.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.emptyShoppingCart));
            this.formSubmitBtn.setDisable(!RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.buyFromShoppingCart));

            final boolean canAddCustomer = RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.searchCustomersByName);
            this.removeButton.setDisable(!canAddCustomer);
            this.selectButton.setDisable(!canAddCustomer);
        }).start();
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void reloadShoppingCart() throws IOException, NotAuthorizedException, ShoppingCartException {
        insertData(RemoteFacade.getInstance().getShoppingCart());
    }

    private void clearCart() {
        this.shoppingCartElements.getChildren().clear();
    }

    private void insertData(ShoppingCartDTO shoppingCartDTO) throws IOException {
        clearCart();
        for (var lineItemDTO : shoppingCartDTO.lineItems()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/article.fxml"));
            Parent medium = fxmlLoader.load();
            ArticleController controller = fxmlLoader.getController();
            controller.addMediumTypes(lineItemDTO, Tabs.SHOPPINGCART);
            this.shoppingCartElements.getChildren().add(medium);
        }
        DecimalFormat df = new DecimalFormat("0.00");
        this.totalAmount.setText(df.format(shoppingCartDTO.totalAmount()));
    }

    @FXML
    private void buyAll(ActionEvent actionEvent) throws IOException, NotAuthorizedException, ShoppingCartException {
        int customer = 0;
        if (!customerNo.getText().equals("")) {
            customer = Integer.parseInt(customerNo.getText());
        }

        Alert confDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confDialog.setContentText("Are you sure you want to make this purchase?");

        ButtonType confirmButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType denyButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        confDialog.getButtonTypes().setAll(confirmButton, denyButton);

        Optional<ButtonType> returnValue = confDialog.showAndWait();
        if (returnValue.isPresent() && returnValue.get() == confirmButton) {
            String invoiceNo = RemoteFacade.getInstance().buyFromShoppingCart(customer);
            new Alert(Alert.AlertType.INFORMATION, "Successfully purchased items\nInvoice number: " + invoiceNo, ButtonType.CLOSE).show();
            reloadShoppingCart();
            removeCustomer();
            appController.selectSearchTab();
        }
    }

    @FXML
    private void removeAll(ActionEvent actionEvent) throws IOException, NotAuthorizedException, ShoppingCartException {
        RemoteFacade.getInstance().emptyShoppingCart();
        reloadShoppingCart();
        removeCustomer();
    }

    public void addCustomer(ActionEvent actionEvent) {
        try {
            AtomicInteger customerNo = new AtomicInteger();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/customer/customer.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            CustomerController controller = fxmlLoader.getController();
            controller.setAtomicInteger(customerNo);
            Stage stage = new Stage();
            stage.setTitle("Select customer");
            stage.setScene(scene);
            stage.showAndWait();
            this.customerNo.setText(customerNo.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void removeCustomer(ActionEvent actionEvent) {
        removeCustomer();
    }

    private void removeCustomer() {
        this.customerNo.setText("");
    }

    @Override
    public void updateShoppingCart() {
        try {
            reloadShoppingCart();
        } catch (IOException | NotAuthorizedException | ShoppingCartException e) {
            clearCart();
        }
    }
}
