package at.fhv.teamd.musicshop.userclient.view.receiveMessage;

import at.fhv.teamd.musicshop.library.DTO.MessageDTO;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.rmi.RemoteException;
import java.util.Set;

public class ReceiveMessageController {

    @FXML
    private TableColumn colDate;

    @FXML
    private TableColumn colTopic;

    @FXML
    private TableColumn colSubject;

    @FXML
    private TableColumn colTrash;

    @FXML
    private TableView<MessageDTO> inbox;
    // TODO: Implement Message Receive (use-> RemoteFacade.getInstance().receiveMessages())

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

        inbox.setItems(FXCollections.observableArrayList(messages));
    }
}
