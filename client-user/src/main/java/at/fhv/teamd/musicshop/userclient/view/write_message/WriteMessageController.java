package at.fhv.teamd.musicshop.userclient.view.write_message;

import at.fhv.teamd.musicshop.library.dto.MessageDTO;
import at.fhv.teamd.musicshop.library.dto.TopicDTO;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.ActivePropertyBindable;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.util.UUID;

public class WriteMessageController implements ActivePropertyBindable {

    @FXML
    private Button formCancelBtn;
    @FXML
    private Button formSubmitBtn;
    @FXML
    private ComboBox<TopicDTO> messageTopic;

    @FXML
    private TextField messageTitle;

    @FXML
    private TextArea messageBody;

    private TopicDTO selectedTopic;

    public void bindActiveProperty(ReadOnlyBooleanProperty activeProp) {
        formCancelBtn.cancelButtonProperty().bind(activeProp);
        formSubmitBtn.defaultButtonProperty().bind(activeProp);
    }

    @FXML
    public void initialize() {
        Platform.runLater(this::initMessageTopic);
    }

    private void initMessageTopic(){
        try {
            if (RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.GET_ALL_TOPICS)) {
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
        } catch (NotAuthorizedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sendMessage(ActionEvent actionEvent) throws NotAuthorizedException, MessagingException {
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
