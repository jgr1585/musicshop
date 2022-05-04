package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.domain.repositories.*;

public class RepositoryFactory {
    private static ArticleRepository articleRepository;

    private static MediumRepository mediumRepository;

    private static EmployeeRepository employeeRepository;

    private static InvoiceRepository invoiceRepository;

    private static TopicRepository topicRepository;

    public static ArticleRepository getArticleRepositoryInstance() {
        return articleRepository;
    }

    public static MediumRepository getMediumRepositoryInstance() {
        return mediumRepository;
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

    public static void setArticleRepository(ArticleRepository articleRepository) {
        RepositoryFactory.articleRepository = articleRepository;
    }

    public static void setMediumRepository(MediumRepository mediumRepository) {
        RepositoryFactory.mediumRepository = mediumRepository;
    }

    public static void setEmployeeRepository(EmployeeRepository employeeRepository) {
        RepositoryFactory.employeeRepository = employeeRepository;
    }

    public static void setInvoiceRepository(InvoiceRepository invoiceRepository) {
        RepositoryFactory.invoiceRepository = invoiceRepository;
    }

    public static void setTopicRepository(TopicRepository topicRepository) {
        RepositoryFactory.topicRepository = topicRepository;
    }
}
