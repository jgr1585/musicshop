package at.fhv.teamd.musicshop.userclient.view.receiveMessage;

import at.fhv.teamd.musicshop.library.DTO.MessageDTO;
import at.fhv.teamd.musicshop.library.DTO.TopicDTO;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class ReceiveMessageController {

    @FXML
    private TableColumn<MessageDTO, String> colDate;

    @FXML
    private TableColumn<MessageDTO, String> colTopic;

    @FXML
    private TableColumn<MessageDTO, String> colSubject;

    @FXML
    private TableColumn<MessageDTO, Button> colTrash;

    @FXML
    private TableView<MessageDTO> inbox;

    private Stage stage;

    @FXML
    public void initialize() {
        this.stage = new Stage();

        // TODO: replace busy-wait
        new Thread(() -> {
            while (true) {
                try {
                    this.loadMessage();
                } catch (MessagingException | NotAuthorizedException | RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void loadMessage() throws RemoteException, MessagingException, NotAuthorizedException {
        //TODO: Fix ReceiveMessageController.receiveMessage()
        //Set<MessageDTO> messages = RemoteFacade.getInstance().receiveMessages();
        Set<MessageDTO> messages = Set.of(
                MessageDTO.builder()
                        .withMessageData(TopicDTO.builder().withTopicData("Test").build(), UUID.randomUUID().toString(), "Test0", "Test")
                        .withMessageSentOnTimestamp(Instant.now())
                        .build(),
                MessageDTO.builder()
                        .withMessageData(TopicDTO.builder().withTopicData("Test").build(), UUID.randomUUID().toString(), "Test1", "Test")
                        .withMessageSentOnTimestamp(Instant.now())
                        .build(),
                MessageDTO.builder()
                        .withMessageData(TopicDTO.builder().withTopicData("Test").build(), UUID.randomUUID().toString(), "Test2", "Test")
                        .withMessageSentOnTimestamp(Instant.now())
                        .build()
        );
        ObservableList<MessageDTO> messageList = FXCollections.observableArrayList(messages);
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.GERMAN)
                .withZone(ZoneId.systemDefault());

        //Set up the columns Formatting
        this.colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(formatter.format(cellData.getValue().sentOnTimestamp())));
        this.colTopic.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().topic().name()));
        this.colSubject.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().title()));
        this.colTrash.setCellValueFactory(cellData -> new SimpleObjectProperty<>(createTrashButton(cellData.getValue())));

        //Add Data to the TableView
        inbox.setItems(messageList);
        inbox.getColumns().addAll(this.colDate, this.colTopic, this.colSubject, this.colTrash);

        inbox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/receiveMessage/message-content.fxml"));
                Parent root = loader.load();
                MessageContentController controller = loader.getController();

                controller.setMessage(newValue);
                this.stage.setScene(new Scene(root));
                this.stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void deleteMessage(MessageDTO message) throws RemoteException, NotAuthorizedException, MessagingException {
        if (message != null) {
            RemoteFacade.getInstance().acknowledgeMessage(message);
        }
    }

    private Button createTrashButton(MessageDTO message) {
        FontAwesomeIconView trashIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        Button trashButton = new Button("", trashIcon);

        trashIcon.setFill(Paint.valueOf("#FF0000"));
        trashButton.setStyle("-fx-background-color: transparent;");

        trashButton.setOnAction(event -> {
            System.out.println("Trash Button Pressed: " + message.title());
            try {
                deleteMessage(message);
            } catch (RemoteException | NotAuthorizedException | MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        return trashButton;
    }
}
