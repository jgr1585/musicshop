package at.fhv.teamd.musicshop.backend.communication;

import at.fhv.teamd.musicshop.library.ApplicationClientFactory;
import at.fhv.teamd.musicshop.library.CustomerDBClient;
import at.fhv.teamd.musicshop.library.CustomerDBClientFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class CustomerClient {
    private static CustomerDBClient customerClient;
    private static final String REMOTE_HOST = "localhost";
    private static final int REMOTE_PORT = 1100;

    public static CustomerDBClient getCustomerClient() throws RemoteException {
        if (customerClient != null) {
            return customerClient;
        }

        try {
            LocateRegistry.getRegistry(REMOTE_HOST, REMOTE_PORT);
            CustomerDBClientFactory customerDBClientFactory = (CustomerDBClientFactory) Naming.lookup("rmi://" + REMOTE_HOST + ":" + REMOTE_PORT + "/CustomerDBClientFactory");
            customerClient = customerDBClientFactory.createCustomerDBClient();
            System.out.println("customerDBClient found");
            return customerClient;
        } catch (MalformedURLException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
