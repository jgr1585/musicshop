package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private MediumRepository mediumRepository;

    private ArticleService articleService;

    @BeforeEach
    public void init() {
        RepositoryFactory.setArticleRepository(this.articleRepository);
        RepositoryFactory.setMediumRepository(this.mediumRepository);
        this.articleService = new ArticleService();
    }

    @Test
    public void given_ArticleService_when_searchArticles_then_returnListOfArticles() throws NoSuchFieldException, IllegalAccessException {
        // given
        Article article = DomainFactory.createArticle();
        String title = article.getTitle();
        String artistName = "";
        Set<Article> articles = new LinkedHashSet<>();
        articles.add(article);

        Mockito.when(this.articleRepository.searchArticlesByAttributes(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString())).thenReturn(articles);

        Set<ArticleDTO> expectedArticleDTOS = articles.stream()
                .map(DTOProvider::buildArticleDTO)
                .collect(Collectors.toSet());

        // when
        AtomicReference<Set<ArticleDTO>> actualArticleDTOS = new AtomicReference<>();

        Assertions.assertDoesNotThrow(() -> actualArticleDTOS.set(this.articleService.searchArticlesByAttributes(title, artistName)));
        Assertions.assertThrows(ApplicationClientException.class, () -> this.articleService.searchArticlesByAttributes(null, null));

        // then
        Assertions.assertTrue(expectedArticleDTOS.containsAll(actualArticleDTOS.get()) && actualArticleDTOS.get().containsAll(expectedArticleDTOS));
    }
}
