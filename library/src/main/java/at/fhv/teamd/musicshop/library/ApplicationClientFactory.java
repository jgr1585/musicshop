package at.fhv.teamd.musicshop.library;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ApplicationClientFactory extends Remote {
    ApplicationClient createApplicationClient()  throws RemoteException;
}
