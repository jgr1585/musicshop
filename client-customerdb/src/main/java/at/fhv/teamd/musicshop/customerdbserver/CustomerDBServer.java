package at.fhv.teamd.musicshop.customerdbserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class CustomerDBServer {
    private static final String LOCAL_HOST = "localhost";
    private static final int LOCAL_PORT = 1100;

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        CustomerDBClientImpl.init();

        LocateRegistry.createRegistry(LOCAL_PORT);
        System.out.println("Registry started @ port " + LOCAL_PORT);

        Naming.rebind("rmi://" + LOCAL_HOST + ":" + LOCAL_PORT + "/CustomerDBClientFactory", new CustomerDBClientFactoryImpl());
        System.out.println("CustomerDBClientFactory bound in registry");
    }
}
