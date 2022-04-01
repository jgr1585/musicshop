package at.fhv.teamd.musicshop.userclient.connection;

import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.ApplicationClientFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIconnection {
    private static ApplicationClient applicationClient;

    static {
        try {
            LocateRegistry.getRegistry("localhost", Registry.REGISTRY_PORT);
            ApplicationClientFactory applicationClientFactory
                    = (ApplicationClientFactory) Naming.lookup("rmi://localhost/ApplicationClientFactoryImpl");
            applicationClient = applicationClientFactory.createApplicationClient();
            System.out.println("ApplicationClient found");
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "No connection to client; Server probably offline?", ButtonType.CLOSE).showAndWait();
            e.printStackTrace();
            System.exit(1);
        }
    }

    private RMIconnection() {}

    public static ApplicationClient getApplicationClient() {
        return applicationClient;
    }
}
