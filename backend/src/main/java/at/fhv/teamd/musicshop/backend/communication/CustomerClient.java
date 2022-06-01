package at.fhv.teamd.musicshop.backend.communication;

import at.fhv.teamd.musicshop.library.CustomerDBClient;
import at.fhv.teamd.musicshop.library.CustomerDBClientFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class CustomerClient {
    private static CustomerDBClient customerDBClient;
    private static final String REMOTE_HOST = "10.0.40.167";
    private static final int REMOTE_PORT = 1100;

    private CustomerClient() {}

    public static CustomerDBClient getCustomerDBClient() throws RemoteException {
        if (customerDBClient != null) {
            return customerDBClient;
        }

        try {
            LocateRegistry.getRegistry(REMOTE_HOST, REMOTE_PORT);
            CustomerDBClientFactory customerDBClientFactory = (CustomerDBClientFactory) Naming.lookup("rmi://" + REMOTE_HOST + ":" + REMOTE_PORT + "/CustomerDBClientFactory");
            customerDBClient = customerDBClientFactory.createCustomerDBClient();
            return customerDBClient;
        } catch (MalformedURLException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
