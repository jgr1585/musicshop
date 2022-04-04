package at.fhv.teamd.musicshop.customerdbserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CustomerDBServer {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        CustomerDBClientImpl.init();
        LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        Naming.rebind("rmi://localhost/CustomerDBClientFactory", new CustomerDBClientFactoryImpl());
        System.out.println("CustomerDBClientFactory bound");
    }
}
