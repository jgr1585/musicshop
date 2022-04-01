package at.fhv.teamd.musicshop.library;

import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CustomerDBClient extends Remote {
    List<CustomerDTO> searchCustomersByName(String name) throws RemoteException, CustomerDBClientException;

    CustomerDTO findCustomerById(int customerId) throws RemoteException, CustomerDBClientException, CustomerNotFoundException;
}
