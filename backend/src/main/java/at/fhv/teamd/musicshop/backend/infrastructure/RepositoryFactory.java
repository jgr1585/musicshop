package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;

public class RepositoryFactory {
    private static ArticleRepository articleRepository;
    private static MediumRepository mediumRepository;

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
}
