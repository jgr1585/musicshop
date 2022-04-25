package at.fhv.teamd.musicshop.userclient.view.writeMessage;

import at.fhv.teamd.musicshop.library.DTO.MessageDTO;
import at.fhv.teamd.musicshop.library.DTO.TopicDTO;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.rmi.RemoteException;

public class WriteMessageController {

    @FXML
    private ComboBox<String> messageTopic;

    @FXML
    private TextField messageTitle;

    @FXML
    private TextArea messageBody;

    private String selectedTopic;

    @FXML
    public void initialize() throws NotAuthorizedException, RemoteException {
        this.initMessageTopic();
    }

    private void initMessageTopic() throws RemoteException, NotAuthorizedException {
        this.messageTopic.setItems(
                FXCollections.observableArrayList(
                        RemoteFacade.getInstance().getAllTopics().stream()
                                .map(TopicDTO::name)
                                .toArray(String[]::new)
                ));

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
                            TopicDTO.builder().withTopicData(selectedTopic).build(),
                            messageTitle.getText(),
                            messageBody.getText())
                    .build();

            try {
                RemoteFacade.getInstance().publishMessage(message);

                new Alert(Alert.AlertType.INFORMATION, "Message successfully sent", ButtonType.CLOSE).show();
                resetMessage();

            } catch (MessagingException e) {
                new Alert(Alert.AlertType.ERROR, "Send message failed", ButtonType.CLOSE).show();;
            }
        }


        // TODO: remove; just an auto-call for testing
        try {
            RemoteFacade.getInstance().receiveMessages();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
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
