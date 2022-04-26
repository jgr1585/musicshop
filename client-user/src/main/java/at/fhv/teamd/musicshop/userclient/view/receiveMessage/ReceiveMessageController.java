package at.fhv.teamd.musicshop.userclient.view.receiveMessage;

import at.fhv.teamd.musicshop.library.DTO.MessageDTO;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Set;

public class ReceiveMessageController {

    @FXML
    private TableColumn<MessageDTO, ?> colDate;

    @FXML
    private TableColumn<MessageDTO, ?> colTopic;

    @FXML
    private TableColumn<MessageDTO, ?> colSubject;

    @FXML
    private TableColumn<MessageDTO, ?> colTrash;

    @FXML
    private TableView<MessageDTO> inbox;

    @FXML
    public void initialize() {
        // TODO: replace busy-wait
        new Thread(() -> {
            while (true) {
                try {
                    this.loadMessage();
                    Thread.sleep(10000);
                } catch (InterruptedException | MessagingException | NotAuthorizedException | RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loadMessage() throws RemoteException, MessagingException, NotAuthorizedException {
        Set<MessageDTO> messages = RemoteFacade.getInstance().receiveMessages();

        System.out.println("looking for messages");
        messages.forEach(System.out::println);

//        this.colTopic.setCellValueFactory(new PropertyValueFactory<>("topic"));
//        this.colSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
//
//        inbox.getColumns().addAll(this.colDate, this.colTopic, this.colSubject, this.colTrash);
//        inbox.setItems(FXCollections.observableArrayList(messages));
//
//        inbox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            Stage stage = new Stage();
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/view/receiveMessage/message-content.fxml"));
//            MessageContentController controller = loader.getController();
//
//            controller.setMessage(newValue);
//
//            try {
//                stage.setScene(loader.load());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            stage.show();
//        });
    }

    private void deleteMessage() throws RemoteException, NotAuthorizedException, MessagingException {
        MessageDTO message = inbox.getSelectionModel().getSelectedItem();
        if (message != null) {
            RemoteFacade.getInstance().acknowledgeMessage(message);
        }
    }
}
