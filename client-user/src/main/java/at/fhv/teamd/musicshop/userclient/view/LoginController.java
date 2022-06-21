package at.fhv.teamd.musicshop.userclient.view;

import at.fhv.teamd.musicshop.library.exceptions.AuthenticationFailedException;
import at.fhv.teamd.musicshop.userclient.Main;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import at.fhv.teamd.musicshop.userclient.observer.LoginSubject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField serverhost;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    private Stage stage;

    @FXML
    private void initialize() {
        this.serverhost.setText("localhost");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void logout() throws IOException {
        LoginSubject.notifyLogout();
        RemoteFacade.destroy();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("templates/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setTitle("MusicShop - Login");
        stage.setScene(scene);

        LoginController controller = fxmlLoader.getController();
        controller.setStage(stage);
    }

    @FXML
    private void onLoginAction(ActionEvent actionEvent) throws IOException {
        try {
            RemoteFacade.authenticateSession(serverhost.getText(), username.getText(), password.getText());

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("templates/main-window.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
            AppController appController = fxmlLoader.getController();
            appController.setLoginController(this);

            stage.setTitle("MusicShop - " + RemoteFacade.getInstance().getSessionUserId());
            stage.setScene(scene);
            LoginSubject.notifyLogin();

        } catch (AuthenticationFailedException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE).show();
            password.setText("");
        }
    }

    @FXML
    private void onResetAction(ActionEvent actionEvent) {
        serverhost.setText("localhost");
        username.setText("");
        password.setText("");
    }
}
