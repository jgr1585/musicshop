package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.EmployeeRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

public class RepositoryFactory {
    private static ArticleRepository articleRepository;

    private static MediumRepository mediumRepository;

    private static EmployeeRepository employeeRepository;


    public static ArticleRepository getArticleRepositoryInstance() {
        return articleRepository;
    }

    public static MediumRepository getMediumRepositoryInstance() {
        return mediumRepository;
    }

    public static EmployeeRepository getEmployeeRepositoryInstance() {
        return employeeRepository;
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
}
