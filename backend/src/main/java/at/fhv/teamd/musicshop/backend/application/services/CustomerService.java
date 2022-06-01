package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.communication.CustomerClient;
import at.fhv.teamd.musicshop.backend.domain.customer.Customer;
import at.fhv.teamd.musicshop.backend.domain.repositories.CustomerRepository;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;

import java.rmi.RemoteException;
import java.util.Set;

public class CustomerService {
    private static final CustomerRepository customerRepository = RepositoryFactory.getCustomerRepositoryInstance();

    public Set<CustomerDTO> searchCustomersByName(String name) throws RemoteException, CustomerDBClientException {
        return CustomerClient.getCustomerDBClient().searchCustomersByName(name);
    }

    public int getCustomerNoByUsername(String username) throws CustomerNotFoundException {
        return customerRepository.findCustomerByUserName(username)
                .orElseThrow(CustomerNotFoundException::new)
                .getCustomerNo();
    }

    public Customer getCustomerByUsername(String username) throws CustomerNotFoundException {
        return customerRepository.findCustomerByUserName(username)
                .orElseThrow(CustomerNotFoundException::new);
    }
}
