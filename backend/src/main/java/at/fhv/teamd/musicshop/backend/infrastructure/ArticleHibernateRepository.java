package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArticleHibernateRepository implements ArticleRepository {

    // package-private constructor to enable initialization only through same package classes
    ArticleHibernateRepository() {
    }

    /*
        Pattern search for the combination of each entered property
        Search is case-insensitive
        Direct matching results have the highest search priority
        A list of maximum 50 results are shown
     */
    @Override
    @Transactional
    public SortedSet<Article> searchArticlesByAttributes(String title, String artist) {
        Objects.requireNonNull(title);
        Objects.requireNonNull(artist);

        EntityManager em = PersistenceManager.getEntityManagerInstance();

        // this query only takes non-empty search criteria into account
        TypedQuery<Article> query = em.createQuery(
                "SELECT DISTINCT(a) FROM Article a JOIN a.artists aa WHERE " +
                        "(:titlePattern <> '%%' OR :artistPattern <> '%%') AND " +
                        "((:titlePattern <> '%%' AND LOWER(a.title) LIKE LOWER(:titlePattern)) OR :titlePattern = '%%') AND " +
                        "((:artistPattern <> '%%' AND LOWER(aa.name) LIKE LOWER(:artistPattern)) OR :artistPattern = '%%')"
                , Article.class).setMaxResults(50);

        String searchTitle = title.trim().toLowerCase();
        String searchArtist = artist.trim().toLowerCase();
        query.setParameter("titlePattern", "%" + searchTitle + "%");
        query.setParameter("artistPattern", "%" + searchArtist + "%");

        Set<Article> articles = new HashSet<>(query.getResultList());

        // NOTE: Alternative to manual sorting may be to order by case select in query
        Function<Article, Integer> scoreByArticleMatchType = article -> {
            String articleTitle = article.getTitle().toLowerCase();

            if (articleTitle.equals(searchTitle) || article.getArtists().stream().anyMatch(art -> art.getName().toLowerCase().equals(searchArtist))) {
                // direct match (case-insensitive)
                return 1;

            } else if (articleTitle.contains(searchTitle) || article.getArtists().stream().anyMatch(art -> art.getName().toLowerCase().contains(searchArtist))) {
                // like-wise match
                return 2;

            } else {
                // (non-match) or match criteria not observed (isEmpty())
                return 3;
            }
        };
        Comparator<Article> compareByArticleMatchType = Comparator.comparing(scoreByArticleMatchType);
        Comparator<Article> compareByArticleAlbumPreference = Comparator.comparing(article -> (article instanceof Album) ? 1 : 2);
        Comparator<Article> compareByArticleId = Comparator.comparing(Article::getId);

        SortedSet<Article> sortedArticles = articles.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(
                        compareByArticleMatchType
                                .thenComparing(compareByArticleAlbumPreference)
                                .thenComparing(compareByArticleId)
                )));

        em.close();

        return sortedArticles;
    }

    @Override
    @Transactional
    public Optional<Article> findArticleById(Long id) {
        Objects.requireNonNull(id);

        EntityManager em = PersistenceManager.getEntityManagerInstance();

        TypedQuery<Article> query = em.createQuery(
                "SELECT a FROM Article a WHERE a.id=:id", Article.class);

        query.setParameter("id", id);

        Optional<Article> articleOpt;

        try {
            articleOpt = Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            articleOpt = Optional.empty();
        }

        em.close();
        return articleOpt;
    }
}
