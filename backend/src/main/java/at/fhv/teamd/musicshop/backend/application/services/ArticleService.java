package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static at.fhv.teamd.musicshop.backend.application.services.DTOProvider.buildArticleDTO;

public class ArticleService {
    private static ArticleRepository articleRepository;
    private static MediumRepository mediumRepository;

    ArticleService() {
        articleRepository = RepositoryFactory.getArticleRepositoryInstance();
        mediumRepository = RepositoryFactory.getMediumRepositoryInstance();
    }

    public Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws ApplicationClientException {
        if (!searchableParam(title, artist)) {
            throw new ApplicationClientException("Validation error: No searchable param for search.");
        }

        return articleRepository.searchArticlesByAttributes(title, artist).stream()
                .map(article -> DTOProvider.buildArticleDTO(mediumRepository, article))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Optional<ArticleDTO> searchArticleByID(Long id) {
        Optional<Article> article = articleRepository.findArticleById(id);

        AtomicReference<Optional<ArticleDTO>> articleDTO = new AtomicReference<>(Optional.empty());

        article.ifPresent(articleItem -> articleDTO.set(Optional.of(buildArticleDTO(mediumRepository, articleItem))));

        return articleDTO.get();
    }

    /*
        At least one field has to be filled
        Minimum length of a field: 1 character
     */
    private static boolean searchableParam(String... params) {
        boolean atLeastOneParamGiven = false;

        for (String param : params) {
            if (param != null && param.length() > 0) {
                atLeastOneParamGiven = true;
                break;
            }
        }

        return atLeastOneParamGiven;
    }
}
