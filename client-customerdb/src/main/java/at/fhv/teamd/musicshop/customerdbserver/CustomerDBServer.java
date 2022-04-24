package at.fhv.teamd.musicshop.customerdbserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class CustomerDBServer {
    public static final int BIND_PORT = 1100;

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        CustomerDBClientImpl.init();

        LocateRegistry.createRegistry(BIND_PORT);
        System.out.println("Registry started @ port " + BIND_PORT);

        Naming.rebind("rmi://localhost:" +BIND_PORT+ "/CustomerDBClientFactory", new CustomerDBClientFactoryImpl());
        System.out.println("CustomerDBClientFactory bound in registry");
    }
}
