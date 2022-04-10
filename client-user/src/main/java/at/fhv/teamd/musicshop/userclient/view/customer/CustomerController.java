package at.fhv.teamd.musicshop.userclient.view.customer;

import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
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

public class CustomerController {
    @FXML
    public TextField search;
    @FXML
    private VBox results;

    public void insertResults(Set<CustomerDTO> results) throws IOException {
        for (var customer : results) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/customer/customer-info.fxml"));
            Parent parent = fxmlLoader.load();
            CustomerInfoController controller = fxmlLoader.getController();
            controller.setFields(customer);
            this.results.getChildren().add(parent);
        }
    }

    public void search(ActionEvent actionEvent) throws IOException, CustomerDBClientException {
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

    public void reset(ActionEvent actionEvent) {
        this.search.setText("");
        this.results.getChildren().clear();
    }
}
