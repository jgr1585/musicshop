package at.fhv.teamd.musicshop.backend.communication;

import at.fhv.teamd.musicshop.backend.application.ApplicationClientFactoryImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class UserClient {

    public static void initRmiRegistry(int port) throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(port);
        System.out.println("Registry started @ port " + port);
        String bindStr = String.format("rmi://%s:%d/%s", "localhost", port, "ApplicationClientFactoryImpl");
        Naming.rebind(bindStr, new ApplicationClientFactoryImpl());
        System.out.println("ApplicationClientFactory bound in registry");
    }
}
