package at.fhv.teamd.musicshop.backend.domain.repositories;

import at.fhv.teamd.musicshop.backend.domain.customer.Customer;

import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findCustomerByUserName(String userName);
}
