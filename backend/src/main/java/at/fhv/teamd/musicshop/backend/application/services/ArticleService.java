package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

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

        SortedSet<Article> articles = articleRepository.searchArticlesByAttributes(title, artist);

        Set<ArticleDTO> articleDTOs = new LinkedHashSet<>();
        for (Article article : articles) {
            articleDTOs.add(buildArticleDTO(mediumRepository, article));
        }

        return articleDTOs;
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
