package at.fhv.teamd.musicshop.userclient.view.receiveMessage;

import at.fhv.teamd.musicshop.library.DTO.MessageDTO;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.Main;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.observer.LoginObserver;
import at.fhv.teamd.musicshop.userclient.observer.LoginSubject;
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
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ReceiveMessageController implements LoginObserver {

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
    private boolean isFirstPoll = true;
    private ScheduledExecutorService executorService;

    public ReceiveMessageController() {
        //Stop the executor service when the stage is closed
        Main.onClose(this::onLogout);
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        this.stage.setTitle("Inbox");
        this.newMessages = new HashSet<>();

        LoginSubject.addObserver(this);

        formatTable();

        new Thread(() -> {
            try {
                this.canAcknowledgeMessage = RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.acknowledgeMessage);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    private void loadMessage() throws RemoteException, MessagingException, NotAuthorizedException {
        System.out.println("poll");
        List<MessageDTO> messages = RemoteFacade.getInstance()
                .receiveMessages().stream()
                .sorted(MessageDTO::compareTo)
                .collect(Collectors.toList());

        this.inbox.getItems().forEach(messages::remove);
        messages.forEach(System.out::println);

        // Add Data to the TableView
        messages.forEach(message -> this.inbox.getItems().add(0, message));

        // updateIcon
        if (!isFirstPoll) {
            this.newMessages.addAll(messages);
            this.updateMessagesTabIcon();
        } else {
            this.isFirstPoll = false;
        }
    }

    private void deleteMessage(MessageDTO message) throws RemoteException, NotAuthorizedException, MessagingException {
        if (message != null) {
            this.inbox.getItems().remove(message);
            RemoteFacade.getInstance().acknowledgeMessage(message);
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
            if (this.newMessages.contains(row.getItem())) {
                row.setStyle(style + "-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #FFFFFF");
            } else {
                row.setStyle(style);
            }
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
                        e.printStackTrace();
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

    @Override
    public void onLogin() {
        try {
            if (RemoteFacade.getInstance().isAuthorizedFor(RemoteFunctionPermission.receiveMessages)) {
                this.executorService = Executors.newSingleThreadScheduledExecutor();

                //Create a scheduled executor service to update the table 10 seconds
                executorService.scheduleAtFixedRate(() -> {
                    try {
                        this.loadMessage();
                    } catch (MessagingException | NotAuthorizedException | RemoteException e) {
                        e.printStackTrace();
                    }
                }, 1, 10, TimeUnit.SECONDS);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLogout() {
        this.executorService.shutdownNow();
    }
}
