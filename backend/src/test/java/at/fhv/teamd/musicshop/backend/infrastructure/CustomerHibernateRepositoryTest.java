package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.domain.customer.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class CustomerHibernateRepositoryTest {
    private CustomerHibernateRepository customerHibernateRepository;

    @BeforeEach
    void init() {
        customerHibernateRepository = new CustomerHibernateRepository();

        BaseRepositoryData.init();
    }

    @Test
    void given_userName_when_findCustomerbyUserName_returnCustomer() {
        //given
        Set<Customer> customers = BaseRepositoryData.getCustomers();
        String userName = customers.iterator().next().getUserName();
        Optional<Customer> expectedCustomer = customers.stream()
                .filter(customer -> customer.getUserName().toLowerCase().contains(userName.toLowerCase()))
                .findFirst();

        //when
        Optional<Customer> actualCustomer = this.customerHibernateRepository.findCustomerByUserName(userName);

        //then
        Assertions.assertEquals(expectedCustomer, actualCustomer);
    }

    @Test
    void given_non_existing_userName_when_findCustomerByUserName_emptyOptional() {
        //given
        Set<Customer> customers = BaseRepositoryData.getCustomers();
        String userName = customers.iterator().next().getUserName() + UUID.randomUUID() + UUID.randomUUID();
        Optional<Customer> expectedCustomer = Optional.empty();

        //when
        Optional<Customer> actualCustomer = this.customerHibernateRepository.findCustomerByUserName(userName);

        //then
        Assertions.assertEquals(expectedCustomer, actualCustomer);
    }
}
