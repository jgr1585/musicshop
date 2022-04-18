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

    @FXML
    public void initialize() {
        this.setMessageTopic();
    }

    private void setMessageTopic() {
        this.messageTopic.setConverter(new StringConverter<Topic>() {
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
