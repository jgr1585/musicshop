package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.backend.communication.UserClient;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class ApplicationServer {
    public static final int RMI_BIND_PORT = Registry.REGISTRY_PORT;

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        UserClient.initRmiRegistry(RMI_BIND_PORT);
    }
}
