package at.fhv.teamd.musicshop.userclient.view.receiveMessage;

import at.fhv.teamd.musicshop.library.DTO.MessageDTO;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.view.AppController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
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
    private Set<MessageDTO> newMessages;
    private AppController appController;

    @FXML
    public void initialize() {
        this.stage = new Stage();
        this.stage.setTitle("Inbox");
        this.newMessages = new HashSet<>();

        formatTable();

        new Thread(() -> {
            try {
                this.canAcknowledgeMessage = RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.acknowledgeMessage);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }).start();


        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        //Stop the executor service when the stage is closed
        executorService.schedule(() -> {
            final Stage stage = (Stage) this.inbox.getScene().getWindow();
            stage.setOnCloseRequest(event -> executorService.shutdown());
        }, 1, TimeUnit.SECONDS);

        //Create a scheduled executor service to update the table 10 seconds
        executorService.scheduleAtFixedRate(() -> {
            try {
                this.loadMessage();
            } catch (MessagingException | NotAuthorizedException | RemoteException e) {
                e.printStackTrace();
            }
        }, 1, 10, TimeUnit.SECONDS);
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    private void loadMessage() throws RemoteException, MessagingException, NotAuthorizedException {
        System.out.println("poll");
        Set<MessageDTO> newMsg = RemoteFacade.getInstance().receiveMessages();

        this.inbox.getItems().forEach(newMsg::remove);
        newMsg.forEach(System.out::println);
        this.newMessages.addAll(newMsg);

        //Add Data to the TableView
        newMsg.forEach(message -> this.inbox.getItems().add(0, message));
        this.updateMessagesTabIcon();
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

        //Create RowFactory for the table
        final String style = "-fx-background-color: transparent; ";

        this.inbox.setRowFactory(tv -> {
            TableRow<MessageDTO> row = new TableRow<>();
            row.setStyle(style + "-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #FFFFFF");
            row.setPrefHeight(25);
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/receiveMessage/message-content.fxml"));
                        Parent root = loader.load();
                        MessageContentController controller = loader.getController();

                        controller.setMessage(this.inbox.getSelectionModel().getSelectedItem());
                        this.stage.setScene(new Scene(root));
                        this.stage.show();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                //Remove from new messages
                row.setStyle(style + "-fx-font-weight: normal;");
                this.newMessages.remove(row.getItem());
                this.updateMessagesTabIcon();
            });
            return row;
        });
    }

    private void updateMessagesTabIcon() {
        if (this.newMessages.size() > 0) {
            this.appController.getReceiveMessageIcon().setIcon(FontAwesomeIcon.ENVELOPE_OPEN);
            this.appController.getReceiveMessageIcon().setFill(Color.RED);
        } else {
            this.appController.getReceiveMessageIcon().setIcon(FontAwesomeIcon.ENVELOPE);
            this.appController.getReceiveMessageIcon().setFill(Color.WHITE);
        }
    }
}
