package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.domain.repositories.*;

public class RepositoryFactory {
    private static ArticleRepository articleRepository;
    private static MediumRepository mediumRepository;
    private static EmployeeRepository employeeRepository;
    private static InvoiceRepository invoiceRepository;
    private static TopicRepository topicRepository;

    public static ArticleRepository getArticleRepositoryInstance() {
        if (articleRepository == null) {
            articleRepository = new ArticleHibernateRepository();
        }
        return articleRepository;
    }

    public static MediumRepository getMediumRepositoryInstance() {
        if (mediumRepository == null) {
            mediumRepository = new MediumHibernateRepository();
        }
        return mediumRepository;
    }

    public static EmployeeRepository getEmployeeRepositoryInstance() {
        if (employeeRepository == null) {
            employeeRepository = new EmployeeHibernateRepository();
        }
        return employeeRepository;
    }

    public static InvoiceRepository getInvoiceRepositoryInstance() {
        if (invoiceRepository == null) {
            invoiceRepository = new InvoiceHibernateRepository();
        }
        return invoiceRepository;
    }

    public static TopicRepository getTopicRepositoryInstance() {
        if (topicRepository == null) {
            topicRepository = new TopicHibernateRepository();
        }
        return topicRepository;
    }
}
