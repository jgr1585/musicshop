package at.fhv.teamd.musicshop.userclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-window.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 700);
            stage.setTitle("MusicShop24");
            stage.setScene(scene);
            stage.show();

        } catch (Throwable e) {
            showError(Thread.currentThread(), e);
        }

        Thread.setDefaultUncaughtExceptionHandler(Main::showError);
    }

    public static void main(String[] args) {
        Application.launch();
    }

    private static void showError(Thread t, Throwable e) {
        if (Platform.isFxApplicationThread()) {
            showErrorDialog(e);
        } else {
            System.err.println("An unexpected error occurred in "+t);
        }
    }

    private static void showErrorDialog(Throwable e) {
        if (isRMIRemoteException(e)) {
            new Alert(Alert.AlertType.ERROR, "Connection to server failed (may be offline).", ButtonType.CLOSE).showAndWait();

        } else {
            StringWriter errorMsg = new StringWriter();
            e.printStackTrace(new PrintWriter(errorMsg));
            new Alert(Alert.AlertType.ERROR, "An exception occurred:\n\n"+errorMsg, ButtonType.CLOSE).showAndWait();
            e.printStackTrace();
        }
    }

    public static boolean isRMIRemoteException(Throwable throwable) {
        Objects.requireNonNull(throwable);
        Throwable cause = throwable;
        while (cause.getCause() != null && cause.getCause() != cause) {
            if (cause instanceof RemoteException) {
                return true;
            }

            cause = cause.getCause();
        }

        return false;
    }
}