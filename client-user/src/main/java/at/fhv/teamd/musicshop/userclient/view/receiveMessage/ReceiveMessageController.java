package at.fhv.teamd.musicshop.userclient.view.receiveMessage;

import at.fhv.teamd.musicshop.library.DTO.MessageDTO;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private boolean canAcknowledgeMessage;

    @FXML
    public void initialize() {
        this.stage = new Stage();
        this.stage.setTitle("Inbox");

        formatTable();

        new Thread(() -> {
            try {
                this.canAcknowledgeMessage = RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.acknowledgeMessage);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }).start();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            try {
                this.loadMessage();
            } catch (MessagingException | NotAuthorizedException | RemoteException e) {
                e.printStackTrace();
            }
        }, 1, 10, TimeUnit.SECONDS);
    }

    private void loadMessage() throws RemoteException, MessagingException, NotAuthorizedException {
        System.out.println("poll");
        Set<MessageDTO> newMessages = RemoteFacade.getInstance().receiveMessages();

        this.inbox.getItems().forEach(newMessages::remove);
        newMessages.forEach(System.out::println);

        ObservableList<MessageDTO> messageList = FXCollections.observableArrayList(newMessages);

        //Add Data to the TableView
        this.inbox.getItems().addAll(messageList);

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
            this.inbox.getItems().remove(message);
        }
    }

    private Button createTrashButton(MessageDTO message) {
        FontAwesomeIconView trashIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        Button trashButton = new Button("", trashIcon);

        trashIcon.setFill(Paint.valueOf("#ff0000"));
        trashButton.setStyle("-fx-background-color: transparent;");
        trashButton.setDisable(!this.canAcknowledgeMessage);

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

    private void formatTable() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.GERMAN);

        //Set up the columns Formatting
        this.colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(formatter.format(LocalDateTime.ofInstant(cellData.getValue().sentOnTimestamp(), ZoneId.systemDefault()))));
        this.colTopic.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().topic().name()));
        this.colSubject.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().title()));
        this.colTrash.setCellValueFactory(cellData -> new SimpleObjectProperty<>(createTrashButton(cellData.getValue())));
    }
}
