package at.fhv.teamd.musicshop.backend.communication;

import at.fhv.teamd.musicshop.backend.application.ApplicationClientFactoryImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class UserClient {
    public static void initRmiRegistry() throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        Naming.rebind("rmi://localhost/ApplicationClientFactoryImpl", new ApplicationClientFactoryImpl());
        System.out.println("ApplicationClientFactoryImpl bound");
    }
}
