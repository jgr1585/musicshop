package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.communication.CustomerClient;
import at.fhv.teamd.musicshop.backend.domain.person.Customer;
import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public class CustomerService {

    public CustomerService() {
    }

    public Set<CustomerDTO> searchCustomersByName(String name) throws RemoteException, CustomerDBClientException {
        return CustomerClient.getCustomerClient().searchCustomersByName(name);
    }

    // TODO: exception?
    // TODO: optional?
    public Customer findCustomerById(int id) throws CustomerDBClientException, CustomerNotFoundException, RemoteException {
        CustomerDTO customerDTO = CustomerClient.getCustomerClient().findCustomerById(id);
        return new Customer(customerDTO.birthName(), customerDTO.givenName(), customerDTO.customerId());
    }
}
