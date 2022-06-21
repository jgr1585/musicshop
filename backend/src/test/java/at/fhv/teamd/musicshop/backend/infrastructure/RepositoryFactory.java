package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.domain.repositories.*;
import org.mockito.Mock;
import org.mockito.Mockito;

public class RepositoryFactory {
    private static final ArticleRepository articleRepository;
    private static final MediumRepository mediumRepository;
    private static final CustomerRepository customerRepository;
    private static final EmployeeRepository employeeRepository;
    private static final InvoiceRepository invoiceRepository;
    private static final TopicRepository topicRepository;

    static {
        articleRepository = Mockito.mock(ArticleRepository.class);
        mediumRepository = Mockito.mock(MediumRepository.class);
        customerRepository = Mockito.mock(CustomerRepository.class);
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        invoiceRepository = Mockito.mock(InvoiceRepository.class);
        topicRepository = Mockito.mock(TopicRepository.class);
    }

    public static ArticleRepository getArticleRepositoryInstance() {
        return articleRepository;
    }

    public static MediumRepository getMediumRepositoryInstance() {
        return mediumRepository;
    }

    public static CustomerRepository getCustomerRepositoryInstance() {
        return customerRepository;
    }

    public static EmployeeRepository getEmployeeRepositoryInstance() {
        return employeeRepository;
    }

    public static InvoiceRepository getInvoiceRepositoryInstance() {
        return invoiceRepository;
    }

    public static TopicRepository getTopicRepositoryInstance() {
        return topicRepository;
    }
}
