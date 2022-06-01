package at.fhv.teamd.musicshop.userclient;

import at.fhv.teamd.musicshop.library.exceptions.InvoiceException;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.library.exceptions.ShoppingCartException;
import at.fhv.teamd.musicshop.userclient.view.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Main extends Application {

    private static final List<Runnable> closeListeners;

    static {
        closeListeners = new LinkedList<>();
    }

    @Override
    public void start(Stage stage) {
        stage.setOnCloseRequest(event -> this.close());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("templates/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
            LoginController controller = fxmlLoader.getController();
            controller.setStage(stage);
            stage.setTitle("MusicShop24 - Login");
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
        Throwable cause;
        if (getThrowableTypeInCauseStack(e, RemoteException.class) != null) {
            new Alert(Alert.AlertType.ERROR, "Connection to server failed (may be offline).", ButtonType.CLOSE).showAndWait();
        } else if ((cause = getThrowableTypeInCauseStack(e, NotAuthorizedException.class)) != null) {
            new Alert(Alert.AlertType.ERROR, cause.getMessage(), ButtonType.CLOSE).showAndWait();
        } else if ((cause = getThrowableTypeInCauseStack(e, ShoppingCartException.class)) != null) {
            new Alert(Alert.AlertType.ERROR, cause.getMessage(), ButtonType.CLOSE).showAndWait();
        } else if ((cause = getThrowableTypeInCauseStack(e, MessagingException.class)) != null) {
            new Alert(Alert.AlertType.ERROR, cause.getMessage(), ButtonType.CLOSE).showAndWait();
        } else if ((cause = getThrowableTypeInCauseStack(e, InvoiceException.class)) != null) {
            new Alert(Alert.AlertType.ERROR, cause.getMessage(), ButtonType.CLOSE).showAndWait();
        } else if ((cause = getThrowableTypeInCauseStack(e, IllegalArgumentException.class)) != null) {
            new Alert(Alert.AlertType.ERROR, cause.getMessage(), ButtonType.CLOSE).showAndWait();
        } else {
            e.printStackTrace();
            // Print stack trace in alert modal
//            StringWriter errorMsg = new StringWriter();
//            e.printStackTrace(new PrintWriter(errorMsg));
//            new Alert(Alert.AlertType.ERROR, "An exception occurred:\n\n"+errorMsg, ButtonType.CLOSE).showAndWait();
            new Alert(Alert.AlertType.ERROR, "An error occurred. Check the error output stream for details.", ButtonType.CLOSE).showAndWait();

            e.printStackTrace();
        }
    }

    public static Throwable getThrowableTypeInCauseStack(Throwable throwable, Class<? extends Throwable> throwableTypeInCauseStack) {
        Objects.requireNonNull(throwable);
        Objects.requireNonNull(throwableTypeInCauseStack);

        Throwable cause = throwable;
        while (cause.getCause() != null && cause.getCause() != cause) {
            if (throwableTypeInCauseStack.isInstance(cause)) {
                return cause;
            }

            cause = cause.getCause();
        }

        if (throwableTypeInCauseStack.isInstance(cause)) {
            return cause;
        }

        return null;
    }

    public static void onClose(Runnable runnable) {
        closeListeners.add(runnable);
    }

    public void close() {
        closeListeners.forEach(Runnable::run);
    }


}