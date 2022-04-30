package at.fhv.teamd.musicshop.userclient.view.article;

import at.fhv.teamd.musicshop.library.DTO.InvoiceDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.userclient.Tabs;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.observer.ReturnObserver;
import at.fhv.teamd.musicshop.userclient.observer.ReturnSubject;
import at.fhv.teamd.musicshop.userclient.observer.ShoppingCartSubject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Set;

public class ReturnController implements ReturnObserver {

    @FXML
    private TextField searchByInvoiceNo;
    @FXML
    private VBox searchPane;

    @FXML
    public void initialize() {
        ReturnSubject.addObserver(this);
    }

    public void reloadReturn() throws IOException, NotAuthorizedException {
        insertResults(RemoteFacade.getInstance().findInvoiceById(Long.valueOf(this.searchByInvoiceNo.getText())).lineItems());
    }

    private void clearSearch() {
        this.searchPane.getChildren().clear();
    }

    public void insertResults(Set<LineItemDTO> results) throws IOException {
        clearSearch();
        for (var lineItem : results) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/article.fxml"));
            Parent medium = fxmlLoader.load();
            ArticleController controller = fxmlLoader.getController();
            controller.addMediumTypes(lineItem, Tabs.RETURN);
            this.searchPane.getChildren().add(medium);
        }
    }

    @FXML
    private void searchInvoiceLineItems(ActionEvent actionEvent) throws IOException, NotAuthorizedException {
        clearSearch();
        if (!(this.searchByInvoiceNo.getText().isEmpty())) {
            InvoiceDTO invoice = RemoteFacade.getInstance().findInvoiceById(Long.valueOf(this.searchByInvoiceNo.getText()));
            if (!(invoice.lineItems().isEmpty())) {
                this.insertResults(invoice.lineItems());
            } else {
                new Alert(Alert.AlertType.NONE, "No articles found", ButtonType.CLOSE).show();
            }
        } else {
            new Alert(Alert.AlertType.NONE, "No parameter for search provided", ButtonType.CLOSE).show();
        }
    }

    @FXML
    private void resetSearch(ActionEvent actionEvent) {
        this.searchPane.getChildren().clear();
        this.searchByInvoiceNo.setText("");
    }

    @Override
    public void updateReturn() {
        try {
            reloadReturn();
        } catch (IOException | NotAuthorizedException e) {
            throw new RuntimeException(e);
        }
    }
}
