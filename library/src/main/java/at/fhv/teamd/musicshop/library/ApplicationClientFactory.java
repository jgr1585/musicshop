package at.fhv.teamd.musicshop.library;

import at.fhv.teamd.musicshop.library.exceptions.AuthenticationFailedException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ApplicationClientFactory extends Remote {
    ApplicationClient createApplicationClient(String authUser, String authPassword) throws RemoteException, AuthenticationFailedException;
}
