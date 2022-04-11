package at.fhv.teamd.musicshop.backend.communication;

import at.fhv.teamd.musicshop.backend.application.ApplicationClientFactoryImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class UserClient {
    private static final int REMOTE_PORT = Registry.REGISTRY_PORT;

    public static void initRmiRegistry() throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(REMOTE_PORT);
        System.out.println("Registry started @ port " + REMOTE_PORT);
        String bindStr = String.format("rmi://%s:%d/%s", "localhost", REMOTE_PORT, "ApplicationClientFactoryImpl");
        Naming.rebind(bindStr, new ApplicationClientFactoryImpl());
        System.out.println("ApplicationClientFactory bound in registry");
    }
}
