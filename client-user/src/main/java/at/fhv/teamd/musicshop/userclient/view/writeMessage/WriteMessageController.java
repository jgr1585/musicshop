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
import javafx.util.StringConverter;

import java.rmi.RemoteException;
import java.util.UUID;

public class WriteMessageController {

    @FXML
    private ComboBox<TopicDTO> messageTopic;

    @FXML
    private TextField messageTitle;

    @FXML
    private TextArea messageBody;

    private TopicDTO selectedTopic;

    @FXML
    public void initialize() throws NotAuthorizedException, RemoteException {
        this.initMessageTopic();
    }

    private void initMessageTopic() throws RemoteException, NotAuthorizedException {
        this.messageTopic.setItems(FXCollections.observableArrayList(RemoteFacade.getInstance().getAllTopics()));
        this.messageTopic.setConverter(new StringConverter<>() {
            @Override
            public String toString(TopicDTO object) {
                return object.name();
            }

            @Override
            public TopicDTO fromString(String string) {
                return null;
            }
        });
        this.messageTopic.valueProperty().addListener((observable, oldValue, newValue) -> this.selectedTopic = newValue);
    }

    @FXML
    private void sendMessage(ActionEvent actionEvent) throws RemoteException, NotAuthorizedException, MessagingException {
        if (this.selectedTopic == null) {
            new Alert(Alert.AlertType.ERROR, "No Topic is selected", ButtonType.CLOSE).show();
        } else if (this.messageTitle.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Message Title is missing", ButtonType.CLOSE).show();
        } else {

            MessageDTO message = MessageDTO.builder()
                    .withMessageData(
                            TopicDTO.builder().withTopicData(selectedTopic.name()).build(),
                            UUID.randomUUID().toString(),
                            messageTitle.getText(),
                            messageBody.getText())
                    .build();

            RemoteFacade.getInstance().publishMessage(message);
            new Alert(Alert.AlertType.INFORMATION, "Message successfully sent", ButtonType.CLOSE).show();
            resetMessage();
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
