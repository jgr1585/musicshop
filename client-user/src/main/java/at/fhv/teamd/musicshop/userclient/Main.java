package at.fhv.teamd.musicshop.userclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("templates/main-window.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
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
        if (containsThrowableTypeInCauseStack(e, RemoteException.class)) {
            new Alert(Alert.AlertType.ERROR, "Connection to server failed (may be offline).", ButtonType.CLOSE).showAndWait();

        } else {
            // Print stack trace in alert modal
//            StringWriter errorMsg = new StringWriter();
//            e.printStackTrace(new PrintWriter(errorMsg));
//            new Alert(Alert.AlertType.ERROR, "An exception occurred:\n\n"+errorMsg, ButtonType.CLOSE).showAndWait();

            new Alert(Alert.AlertType.ERROR, "An error occurred. Check the error output stream for details.", ButtonType.CLOSE).showAndWait();

            e.printStackTrace();
        }
    }

    public static boolean containsThrowableTypeInCauseStack(Throwable throwable, Class<? extends Throwable> throwableTypeInCauseStack) {
        Objects.requireNonNull(throwable);
        Objects.requireNonNull(throwableTypeInCauseStack);

        Throwable cause = throwable;
        while (cause.getCause() != null && cause.getCause() != cause) {
            if (throwableTypeInCauseStack.isInstance(cause)) {
                return true;
            }

            cause = cause.getCause();
        }

        return false;
    }
}