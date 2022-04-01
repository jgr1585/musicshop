package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.ApplicationClientFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ApplicationClientFactoryImpl extends UnicastRemoteObject implements ApplicationClientFactory {

    public ApplicationClientFactoryImpl() throws RemoteException {
        super();
    }

    @Override
    public ApplicationClient createApplicationClient() throws RemoteException {
        return new ApplicationClientImpl();
    }
}
