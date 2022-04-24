package at.fhv.teamd.musicshop.userclient.view.customer;

import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerController {
    @FXML
    private TextField search;
    @FXML
    private VBox results;

    private AtomicInteger customerNo;

    private void insertResults(Set<CustomerDTO> results) throws IOException {
        for (var customer : results) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/customer/customer-info.fxml"));
            Parent parent = fxmlLoader.load();
            CustomerInfoController controller = fxmlLoader.getController();
            controller.setFields(customer, customerNo);
            this.results.getChildren().add(parent);
        }
    }

    @FXML
    private void search(ActionEvent actionEvent) throws IOException, CustomerDBClientException, NotAuthorizedException {
        this.results.getChildren().clear();
        if (!(this.search.getText().isEmpty())) {
            Set<CustomerDTO> result = RemoteFacade.getInstance().searchCustomersByName(this.search.getText());
            if (!(result.isEmpty())) {
                this.insertResults(result);
            } else {
                new Alert(Alert.AlertType.NONE, "No customers found", ButtonType.CLOSE).show();
            }
        } else {
            new Alert(Alert.AlertType.NONE, "No parameter for search provided", ButtonType.CLOSE).show();
        }
    }

    @FXML
    private void reset(ActionEvent actionEvent) {
        this.search.setText("");
        this.results.getChildren().clear();
    }

    public void setAtomicInteger(AtomicInteger atomicInteger) {
        this.customerNo = atomicInteger;
    }
}
