package at.fhv.teamd.musicshop.userclient.view.receiveMessage;

import at.fhv.teamd.musicshop.library.DTO.MessageDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MessageContentController {

    @FXML
    private TextField topic;

    @FXML
    private TextField subject;

    @FXML
    private TextField date;

    @FXML
    private TextArea messageBody;

    public void setMessage(MessageDTO message) {
        topic.setText(message.topic().name());
        subject.setText(message.title());
        messageBody.setText(message.body());
    }

}
