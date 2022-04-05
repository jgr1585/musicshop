package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
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
    public void given_ArticleService_when_searchArticles_then_returnListOfArticles() {
        // given
        Article article = DomainFactory.createArticle();
        String title = article.getTitle();
        String artistName = "";
        Set<Article> articles = Set.of(article);

        Mockito.when(this.articleRepository.searchArticlesByAttributes(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString())).thenReturn(articles);

        System.out.println(this.articleRepository.searchArticlesByAttributes(title, artistName));

        //noinspection OptionalGetWithoutIsPresent
        Mockito.when(this.mediumRepository.findMediumById(article.getMediumIDs().stream().findFirst().get())).thenReturn(article.getMediums().stream().findFirst());

        Set<ArticleDTO> expectedArticleDTOS = articles.stream()
                .map(art -> DTOProvider.buildArticleDTO(this.mediumRepository, art))
                .collect(Collectors.toSet());

        // when
        AtomicReference<Set<ArticleDTO>> actualArticleDTOS = new AtomicReference<>();

        Assertions.assertDoesNotThrow(() -> actualArticleDTOS.set(this.articleService.searchArticlesByAttributes(title, artistName)));
        Assertions.assertThrows(ApplicationClientException.class, () -> this.articleService.searchArticlesByAttributes(null, null));

        // then
        Assertions.assertTrue(expectedArticleDTOS.containsAll(actualArticleDTOS.get()) && actualArticleDTOS.get().containsAll(expectedArticleDTOS));

    }

    @Test
    public void given_ArticleService_when_getArticleById_then_returnArticle() {
        // given
        Article article = DomainFactory.createArticle();
        Long id = article.getId();

        Mockito.when(this.articleRepository.findArticleById(id)).thenReturn(Optional.of(article));

        //noinspection OptionalGetWithoutIsPresent
        Mockito.when(this.mediumRepository.findMediumById(article.getMediumIDs().stream().findFirst().get())).thenReturn(article.getMediums().stream().findFirst());

        // when
        Optional<ArticleDTO> actualArticleDTO;

        actualArticleDTO = this.articleService.searchArticleByID(id);

        // then
        Assertions.assertTrue(actualArticleDTO.isPresent());
        Assertions.assertEquals(DTOProvider.buildArticleDTO(this.mediumRepository, article), actualArticleDTO.get());
    }

}
