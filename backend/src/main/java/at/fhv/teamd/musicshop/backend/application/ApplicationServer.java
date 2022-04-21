package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.backend.communication.UserClient;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class ApplicationServer {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        UserClient.initRmiRegistry();
    }
}
