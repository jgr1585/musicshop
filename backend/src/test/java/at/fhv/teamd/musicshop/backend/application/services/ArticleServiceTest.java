package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.dto.ArticleDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    private final ArticleRepository articleRepository;

    private final ArticleService articleService;

    private ArticleServiceTest() {
        this.articleRepository = RepositoryFactory.getArticleRepositoryInstance();
        this.articleService = new ArticleService();
    }

    @Test
    void given_ArticleService_when_searchArticles_then_returnListOfArticles() {
        // given
        Article article = DomainFactory.createArticle();
        String title = article.getTitle();
        String artistName = "";
        Set<Article> articles = Set.of(article);

        Mockito.when(this.articleRepository.searchArticlesByAttributes(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString())).thenReturn(articles);

        Set<ArticleDTO> expectedArticleDTOS = articles.stream()
                .map(DTOProvider::buildArticleDTO)
                .collect(Collectors.toSet());

        // when
        AtomicReference<Set<ArticleDTO>> actualArticleDTOS = new AtomicReference<>();

        Assertions.assertDoesNotThrow(() -> actualArticleDTOS.set(this.articleService.searchPhysicalMediumArticles(title, artistName)));
        Assertions.assertThrows(ApplicationClientException.class, () -> this.articleService.searchPhysicalMediumArticles(null, null));

        // then
        Assertions.assertTrue(expectedArticleDTOS.containsAll(actualArticleDTOS.get()) && actualArticleDTOS.get().containsAll(expectedArticleDTOS));
    }
}
