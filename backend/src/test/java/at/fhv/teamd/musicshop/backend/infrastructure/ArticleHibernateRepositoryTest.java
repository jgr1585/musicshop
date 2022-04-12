package at.fhv.teamd.musicshop.backend.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleHibernateRepositoryTest {
    private ArticleHibernateRepository articleHibernateRepository;

    @BeforeEach
    void init() {
        articleHibernateRepository = new ArticleHibernateRepository();
    }

    @Test
    void searchArticlesByAttributes() {

    }

}