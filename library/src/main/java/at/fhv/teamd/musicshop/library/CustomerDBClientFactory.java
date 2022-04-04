package at.fhv.teamd.musicshop.library;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CustomerDBClientFactory extends Remote {
    CustomerDBClient createCustomerDBClient() throws RemoteException;
}
