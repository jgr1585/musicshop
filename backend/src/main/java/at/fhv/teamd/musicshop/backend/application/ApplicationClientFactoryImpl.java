package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.ApplicationClientFactory;
import at.fhv.teamd.musicshop.library.exceptions.AuthenticationFailedException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ApplicationClientFactoryImpl extends UnicastRemoteObject implements ApplicationClientFactory {

    public ApplicationClientFactoryImpl() throws RemoteException {
        super(ApplicationServer.RMI_BIND_PORT);
    }

    @Override
    public ApplicationClient createApplicationClient(String authUser, String authPassword) throws RemoteException, AuthenticationFailedException {
        return ApplicationClientImpl.newInstance(authUser, authPassword);
    }
}
