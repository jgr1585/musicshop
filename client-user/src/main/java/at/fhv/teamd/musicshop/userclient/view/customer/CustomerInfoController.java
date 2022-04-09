package at.fhv.teamd.musicshop.userclient.view.customer;

import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CustomerInfoController {
    @FXML
    private Label firstName;

    @FXML
    private Label lastName;

    @FXML
    private Label birthday;

    @FXML
    private Label id;

    public int select(ActionEvent actionEvent) {
        return Integer.parseInt(this.id.getText());
    }

    public void setFields(CustomerDTO customer) {
        this.firstName.setText(customer.givenName());
        this.lastName.setText(customer.familyName());
        this.birthday.setText(customer.birthDate());
        this.id.setText(customer.customerId().toString());
    }
}
