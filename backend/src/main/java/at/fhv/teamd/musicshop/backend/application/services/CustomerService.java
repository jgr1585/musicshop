package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.communication.CustomerClient;
import at.fhv.teamd.musicshop.library.dto.CustomerDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;

import java.rmi.RemoteException;
import java.util.Set;

public class CustomerService {

    public CustomerService() {
    }

    public Set<CustomerDTO> searchCustomersByName(String name) throws RemoteException, CustomerDBClientException {
        return CustomerClient.getCustomerClient().searchCustomersByName(name);
    }
}
