package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.communication.CustomerClient;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.person.Customer;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.CustomerDBClient;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static at.fhv.teamd.musicshop.backend.application.services.DTOProvider.buildArticleDTO;

public class CustomerService {

    // TODO: exception?
    // TODO: static?
    public static Customer searchCustomerById(int id) throws MalformedURLException, NotBoundException, CustomerDBClientException, CustomerNotFoundException, RemoteException {
        CustomerClient.initRmiRegistry();

//        if (!searchableParam(title, artist)) {
//            throw new ApplicationClientException("Validation error: No searchable param for search.");
//        }

        CustomerDTO customerDTO = CustomerClient.getCustomerClient().findCustomerById(id);
        return new Customer(customerDTO.birthName(), customerDTO.givenName(), customerDTO.customerId());
    }
}
