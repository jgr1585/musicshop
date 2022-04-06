package at.fhv.teamd.musicshop.backend.communication;

import at.fhv.teamd.musicshop.library.CustomerDBClient;
import at.fhv.teamd.musicshop.library.CustomerDBClientFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CustomerClient {
    private static CustomerDBClient customerClient;
    public static void initRmiRegistry() throws RemoteException, MalformedURLException, NotBoundException {
        LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
        CustomerDBClientFactory customerDBClientFactory = (CustomerDBClientFactory) Naming.lookup("rmi://localhost/CustomerDBClientFactory");
        customerClient = customerDBClientFactory.createCustomerDBClient();
        System.out.println("customerDBClient found");
    }
    public static CustomerDBClient getCustomerClient() {
        return customerClient;
    }
}
