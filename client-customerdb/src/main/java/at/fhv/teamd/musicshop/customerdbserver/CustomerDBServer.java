package at.fhv.teamd.musicshop.customerdbserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CustomerDBServer {
    private static final int REMOTE_PORT = 1100;

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        CustomerDBClientImpl.init();

        LocateRegistry.createRegistry(REMOTE_PORT);
        System.out.println("Registry started @ port " + REMOTE_PORT);

        String bindStr = String.format("rmi://%s:%d/%s", "localhost", REMOTE_PORT, "CustomerDBClientFactory");
        Naming.rebind(bindStr, new CustomerDBClientFactoryImpl());
        System.out.println("CustomerDBClientFactory bound in registry");
    }
}
