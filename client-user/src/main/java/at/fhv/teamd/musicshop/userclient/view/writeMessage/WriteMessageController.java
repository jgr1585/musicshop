package at.fhv.teamd.musicshop.userclient.view.writeMessage;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

public class WriteMessageController {

    @FXML
    private ComboBox<Topic> messageTopic;
    @FXML
    private TextField messageTitle;

    @FXML
    private TextArea messageBody;

    private Topic selectedTopic;

    @FXML
    public void initialize() {
        this.initMessageTopic();
    }

    private void initMessageTopic() {
        this.messageTopic.setConverter(new StringConverter<>() {
            @Override
            public String toString(Topic topic) {
                return topic.getName();
            }

            @Override
            public Topic fromString(String string) {
                return null;
            }
        });
        this.messageTopic.setItems(FXCollections.observableArrayList(Topic.values()));

        this.messageTopic.valueProperty().addListener((observable, oldValue, newValue) -> this.selectedTopic = newValue);
    }

    @FXML
    private void resetMessage(ActionEvent actionEvent) {
        this.messageTitle.setText("");
        this.messageBody.setText("");
    }

    @FXML
    private void sendMessage(ActionEvent actionEvent) {
        if (this.selectedTopic == null) {
            new Alert(Alert.AlertType.ERROR, "No Topic is selected", ButtonType.CLOSE).show();
        }
        else if (this.messageTitle.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Message Title is missing", ButtonType.CLOSE).show();
        }
        //TODO: after implementation of Backend
    }
}
