package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.AlbumDTO;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ArticleService {
    private static final ArticleRepository articleRepository = RepositoryFactory.getArticleRepositoryInstance();

    public Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws ApplicationClientException {
        if (!searchableParam(title, artist)) {
            throw new ApplicationClientException("Validation error: No searchable param for search.");
        }

        return articleRepository.searchArticlesByAttributes(title, artist).stream()
                .map(DTOProvider::buildArticleDTO)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Optional<AlbumDTO> getAlbumById(long id) throws ApplicationClientException {
        if (id < 1) {
            throw new ApplicationClientException("Validation error: Id must be greater than 0.");
        }

        Article article = articleRepository.findArticleById(id).filter(Album.class::isInstance).orElse(null);
        Album album = (Album) article;

        if (album != null) {
            return Optional.of(DTOProvider.buildArticleDTO(album));
        } else {
            return Optional.empty();
        }
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
