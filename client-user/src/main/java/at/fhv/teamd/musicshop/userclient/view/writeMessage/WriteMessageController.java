package at.fhv.teamd.musicshop.userclient.view.writeMessage;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
        //TODO: after implementation of Backend
    }
}
