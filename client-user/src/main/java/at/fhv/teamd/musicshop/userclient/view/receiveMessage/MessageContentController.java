package at.fhv.teamd.musicshop.userclient.view.receiveMessage;

import at.fhv.teamd.musicshop.library.DTO.MessageDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.GERMAN);

        LocalDateTime test = LocalDateTime.ofEpochSecond(message.sentOnTimestamp().getEpochSecond(), message.sentOnTimestamp().getNano(), ZoneOffset.UTC);
        System.out.println(test);

        topic.setText(message.topic().name());
        subject.setText(message.title());
        messageBody.setText(message.body());
        date.setText(formatter.format(LocalDateTime.ofInstant(message.sentOnTimestamp(), ZoneId.systemDefault())));
    }

}
