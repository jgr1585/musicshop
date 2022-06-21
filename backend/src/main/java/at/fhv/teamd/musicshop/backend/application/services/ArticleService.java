package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.dto.ArticleDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class ArticleService {
    private final ArticleRepository articleRepository = RepositoryFactory.getArticleRepositoryInstance();

    ArticleService() {
    }

    // manual filtration by medium type requirement (any)
    // -> filter only articles which are/have an album with at least one filteredMediumType contained
    // - album: medium for any filteredMediumType must exist
    // - song: album with a medium for any filteredMediumType must exit
    private Set<Article> filterArticlesByAnyAlbumMediumType(Set<Article> articles, EnumSet<MediumType> mediumTypes) {

        BiPredicate<Article, EnumSet<MediumType>> articleContainsAnyAlbumMediumTypePredicate = (article, mts) -> {
            if (article instanceof Album) {
                return hasAlbumAnyMediumType((Album) article, mts);

            } else if (article instanceof Song) {
                return ((Song) article).getAlbums().stream()
                        .anyMatch(album -> hasAlbumAnyMediumType(album, mts));

            } else {
                return false;
            }
        };

        return articles.stream()
                .filter(article -> articleContainsAnyAlbumMediumTypePredicate.test(article, mediumTypes))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private boolean hasAlbumAnyMediumType(Album album, EnumSet<MediumType> mediumTypes) {
        return mediumTypes.stream()
                .anyMatch(mt -> album.getMediums().stream().anyMatch(medium -> medium.getType() == mt));
    }

    public Set<ArticleDTO> searchPhysicalMediumArticles(String title, String artist) throws ApplicationClientException {
        if (!searchableParam(title, artist)) {
            throw new ApplicationClientException("Validation error: No searchable param for search.");
        }

        EnumSet<MediumType> filteredMediumTypes = MediumType.analogMediumTypes();
        return filterArticlesByAnyAlbumMediumType(articleRepository.searchArticlesByAttributes(title, artist), filteredMediumTypes).stream()
                .map(article -> DTOProvider.buildArticleDTOWithMediumTypes(article, filteredMediumTypes))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<ArticleDTO> searchDigitalMediumArticles(String title, String artist) throws ApplicationClientException {
        if (!searchableParam(title, artist)) {
            throw new ApplicationClientException("Validation error: No searchable param for search.");
        }

        EnumSet<MediumType> filteredMediumTypes = MediumType.digitalMediumTypes();
        return filterArticlesByAnyAlbumMediumType(articleRepository.searchArticlesByAttributes(title, artist), filteredMediumTypes).stream()
                .map(article -> DTOProvider.buildArticleDTOWithMediumTypes(article, filteredMediumTypes))
                .collect(Collectors.toCollection(LinkedHashSet::new));
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
