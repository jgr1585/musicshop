package at.fhv.teamd.musicshop.customerdbserver;


import at.fhv.teamd.musicshop.library.CustomerDBClient;
import at.fhv.teamd.musicshop.library.CustomerDBClientFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CustomerDBClientFactoryImpl extends UnicastRemoteObject implements CustomerDBClientFactory {

    public CustomerDBClientFactoryImpl() throws RemoteException {
        super(CustomerDBServer.BIND_PORT);
    }

    @Override
    public CustomerDBClient createCustomerDBClient() throws RemoteException {
        return new CustomerDBClientImpl();
    }
}
