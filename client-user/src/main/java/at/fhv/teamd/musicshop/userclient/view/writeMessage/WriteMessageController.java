package at.fhv.teamd.musicshop.userclient.view.writeMessage;

import at.fhv.teamd.musicshop.library.DTO.MessageDTO;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.rmi.RemoteException;
import java.util.Arrays;

public class WriteMessageController {

    @FXML
    private ComboBox<String> messageTopic;

    @FXML
    private TextField messageTitle;

    @FXML
    private TextArea messageBody;

    private String selectedTopic;

    @FXML
    public void initialize() {
        this.initMessageTopic();
    }

    private void initMessageTopic() {
        //TODO: Get Topics from Backend
//        this.messageTopic.setItems(FXCollections.observableArrayList(Arrays.stream(Topic.values()).map(Topic::getName).toArray(String[]::new)));

        this.messageTopic.valueProperty().addListener((observable, oldValue, newValue) -> this.selectedTopic = newValue);
    }

    @FXML
    private void sendMessage(ActionEvent actionEvent) throws RemoteException, NotAuthorizedException {
        if (this.selectedTopic == null) {
            new Alert(Alert.AlertType.ERROR, "No Topic is selected", ButtonType.CLOSE).show();
        } else if (this.messageTitle.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Message Title is missing", ButtonType.CLOSE).show();
        } else {

            MessageDTO message = MessageDTO.builder()
                    .withMessageData(
                            selectedTopic,
                            messageTitle.getText(),
                            messageBody.getText())
                    .build();

            if (RemoteFacade.getInstance().publishMessage(message)) {
                new Alert(Alert.AlertType.INFORMATION, "Message successfully sent", ButtonType.CLOSE).show();
                resetMessage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Send message failed", ButtonType.CLOSE).show();
            }
        }
    }

    @FXML
    private void resetMessage(ActionEvent actionEvent) {
        resetMessage();
    }

    private void resetMessage() {
        this.messageTopic.getSelectionModel().clearSelection();
        this.selectedTopic = null;
        this.messageTitle.setText("");
        this.messageBody.setText("");
    }
}
