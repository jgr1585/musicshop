package at.fhv.teamd.musicshop.userclient.view.customer;

import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;

public class CustomerInfoController {

    @FXML
    private Label customerNo;
    @FXML
    private Label firstName;
    @FXML
    private Label lastName;
    @FXML
    public Label country;
    @FXML
    public Label street;
    @FXML
    public Label zipcode;

    private AtomicInteger atomicInteger;

    @FXML
    private void select(ActionEvent actionEvent) {
        atomicInteger.set(Integer.parseInt(this.customerNo.getText()));
        close();
    }

    public void setFields(CustomerDTO customer, AtomicInteger atomicInteger) {
        this.firstName.setText(customer.givenName());
        this.lastName.setText(customer.familyName());
        this.customerNo.setText(customer.customerId().toString());
        this.atomicInteger = atomicInteger;
        this.country.setText(customer.addressCountry());
        this.street.setText(customer.addressStreet() + " " + customer.addressHouseNumber());
        this.zipcode.setText(customer.addressPostalCode());
    }

    private void close() {
        Stage stage = (Stage) customerNo.getScene().getWindow();
        stage.close();
    }
}
