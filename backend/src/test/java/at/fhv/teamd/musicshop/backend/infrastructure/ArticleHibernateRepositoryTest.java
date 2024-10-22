package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.domain.article.Article;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

class ArticleHibernateRepositoryTest {
    private ArticleHibernateRepository articleHibernateRepository;

    @BeforeEach
    public void init() {
        this.articleHibernateRepository = new ArticleHibernateRepository();

        BaseRepositoryData.init();
    }

    @Test
    void given_articleAttribute_when_searchArticleByAttributes_returnSortedSet() {
        //given
        Set<Article> articles = BaseRepositoryData.getArticles();
        String title = articles.iterator().next().getTitle();
        String artist = articles.iterator().next().getArtists().iterator().next().getName();

        Set<Article> expectedArticles1 = articles.stream()
                .filter(article -> article.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toSet());

        Set<Article> expectedArticles2 = articles.stream()
                .filter(article -> article.getArtists().stream().anyMatch(artist1 -> artist1.getName().equalsIgnoreCase(artist)))
                .collect(Collectors.toSet());

        //when
        Set<Article> actualArticles1 = this.articleHibernateRepository.searchArticlesByAttributes(title, "");
        Set<Article> actualArticles2 = this.articleHibernateRepository.searchArticlesByAttributes("", artist);

        //then
        Assertions.assertTrue(expectedArticles1.containsAll(actualArticles1));
        Assertions.assertTrue(actualArticles1.containsAll(expectedArticles1));
        Assertions.assertTrue(expectedArticles2.containsAll(actualArticles2));
        Assertions.assertTrue(actualArticles2.containsAll(expectedArticles2));
    }

    @Test
    void given_likewise_matching_articleAttribute_when_searchArticleByAttributes_returnSortedSet() {
        //given
        Set<Article> articles = BaseRepositoryData.getArticles();
        String title = articles.iterator().next().getTitle().substring(0,3);
        String artist = articles.iterator().next().getArtists().iterator().next().getName().substring(0,2);

        Set<Article> expectedArticles1 = articles.stream()
                .filter(article -> article.getTitle().toUpperCase().contains(title.toUpperCase()))
                .collect(Collectors.toSet());

        Set<Article> expectedArticles2 = articles.stream()
                .filter(article -> article.getArtists().stream().anyMatch(artist1 -> artist1.getName().toLowerCase().contains(artist.toLowerCase())))
                .collect(Collectors.toSet());

        //when
        Set<Article> actualArticles1 = this.articleHibernateRepository.searchArticlesByAttributes(title, "");
        Set<Article> actualArticles2 = this.articleHibernateRepository.searchArticlesByAttributes("", artist);

        //then
        Assertions.assertTrue(expectedArticles1.containsAll(actualArticles1));
        Assertions.assertTrue(actualArticles1.containsAll(expectedArticles1));
        Assertions.assertTrue(expectedArticles2.containsAll(actualArticles2));
        Assertions.assertTrue(actualArticles2.containsAll(expectedArticles2));
    }

    @Test
    void given_non_existing_articleAttribute_when_searchArticleByAttributes_returnEmpty() {
        //given
        Set<Article> articles = BaseRepositoryData.getArticles();
        String title = articles.iterator().next().getTitle() + UUID.randomUUID();
        String artist = articles.iterator().next().getArtists().iterator().next().getName() + UUID.randomUUID();

        Set<Article> expectedArticles1 = Set.of();

        Set<Article> expectedArticles2 = Set.of();

        //when
        Set<Article> actualArticles1 = this.articleHibernateRepository.searchArticlesByAttributes(title, "");
        Set<Article> actualArticles2 = this.articleHibernateRepository.searchArticlesByAttributes("", artist);

        //then
        Assertions.assertTrue(expectedArticles1.containsAll(actualArticles1));
        Assertions.assertTrue(actualArticles1.containsAll(expectedArticles1));
        Assertions.assertTrue(expectedArticles2.containsAll(actualArticles2));
        Assertions.assertTrue(actualArticles2.containsAll(expectedArticles2));
    }

    @Test
    void given_articleId_when_findArticleById_returnArticle() {
        //given
        Set<Article> articles = BaseRepositoryData.getArticles();
        long id = articles.iterator().next().getId();

        Optional<Article> expectedArticles = articles.stream()
                .filter(article -> article.getId() == id)
                .findFirst();

        //when
        Optional<Article> actualArticles = this.articleHibernateRepository.findArticleById(id);

        //then
        Assertions.assertEquals(expectedArticles, actualArticles);
    }

    @Test
    void given_non_existing_articleId_when_findArticleById_returnArticle() {
        //given
        Set<Article> articles = BaseRepositoryData.getArticles();
        long id = articles.stream()
                .mapToLong(Article::getId)
                .max().orElseThrow() +1;

        Optional<Article> expectedArticles = Optional.empty();

        //when
        Optional<Article> actualArticles = this.articleHibernateRepository.findArticleById(id);

        //then
        Assertions.assertEquals(expectedArticles, actualArticles);
    }
}