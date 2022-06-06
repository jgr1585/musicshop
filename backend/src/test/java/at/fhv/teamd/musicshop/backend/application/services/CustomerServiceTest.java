package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.communication.CustomerClient;
import at.fhv.teamd.musicshop.backend.domain.customer.Customer;
import at.fhv.teamd.musicshop.library.dto.CustomerDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {


    private CustomerService customerService;

    private CustomerClient customerClient;

    @Test
    void given_CustomerService_when_searchCustomerById_then_returnCustomer() throws RemoteException, CustomerDBClientException, CustomerNotFoundException {
        // given
        CustomerDTO customer = CustomerClient.getCustomerDBClient().findCustomerById(1);

        // when
        CustomerDTO actualCustomerDTO;

//        actualCustomerDTO = this.customerClient.
        //actualCustomerDTO = this.customerService.searchArticleByID(id);

        // then

        //Assertions.assertTrue(actualArticleDTO.isPresent());
        //Assertions.assertEquals(DTOProvider.buildArticleDTO(this.mediumRepository, customer), actualArticleDTO.get());
    }



}