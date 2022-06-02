package at.fhv.teamd.musicshop.library;

import at.fhv.teamd.musicshop.library.dto.CustomerDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface CustomerDBClient extends Remote {
    Set<CustomerDTO> searchCustomersByName(String name) throws RemoteException, CustomerDBClientException;
    CustomerDTO findCustomerById(int customerId) throws RemoteException, CustomerDBClientException, CustomerNotFoundException;
}
