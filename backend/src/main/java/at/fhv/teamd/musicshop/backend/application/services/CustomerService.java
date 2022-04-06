package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.communication.CustomerClient;
import at.fhv.teamd.musicshop.backend.domain.person.Customer;
import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class CustomerService {

    // TODO: exception?
    // TODO: optional?
    // TODO: static?
    public static Customer findCustomerById(int id) throws MalformedURLException, NotBoundException, CustomerDBClientException, CustomerNotFoundException, RemoteException {
        CustomerClient.initRmiRegistry();
        CustomerDTO customerDTO = CustomerClient.getCustomerClient().findCustomerById(id);
        return new Customer(customerDTO.birthName(), customerDTO.givenName(), customerDTO.customerId());
    }
}
