package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

public class ArticleHibernateRepository implements ArticleRepository {

    // package-private constructor to enable initialization only through same package classes
    ArticleHibernateRepository() {}

    /*
        Pattern search for the combination of each entered property
        Search is case-insensitive
        Direct matching results have the highest search priority
        A list of maximum 50 results are shown
     */
    @Override
    @Transactional
    public Set<Article> searchArticlesByAttributes(String title, String artist) {
        Objects.requireNonNull(title);
        Objects.requireNonNull(artist);

        EntityManager em = PersistenceManager.getEntityManagerInstance();

        // TODO: Suche nach artist einbauen
        //  (Tipp: TREAT könnte man verwenden, um auf Subtypen zu selektieren.
        //  Allerdings stellt sich die Frage, ob es nicht besser wäre, wenn man direkt im Artikel nach Artists
        //  selektieren könnte, da Album und Songs Artists haben, die Albums aber lediglich die Artists der Songs
        //  aggregieren.)
        // TODO: Order by case (?) verwenden, um die Direkttreffer (case-insensitive) vorzureihen
        TypedQuery<Article> query = em.createQuery(
                "SELECT DISTINCT(a) FROM Article a WHERE " +
                        "((a.title <> '' AND LOWER(a.title) LIKE LOWER(:titlePattern)) OR a.title = '')"
                , Article.class).setMaxResults(50);

        query.setParameter("titlePattern", "%"+title+"%");
//        query.setParameter("artistPattern", "%"+artist+"%");

        // NOTE: Alternative to this is to order by case select in query
        Set<Article> articles = new HashSet<>(query.getResultList());

        Set<Article> articlesDirectMatches = articles.stream()
                .filter(article -> article.getTitle().equals(title)
//                                || article.getArtists().stream().anyMatch(
//                                        albumArtist -> albumArtist.getName().equals(artist)
//                                    )
                )
                .collect(Collectors.toSet());

        Set<Article> articlesIndirectMatches = articles.stream()
                .filter(article -> !articlesDirectMatches.contains(article))
                .collect(Collectors.toSet());


        articles.addAll(articlesDirectMatches);
        articles.addAll(articlesIndirectMatches);

        em.close();
        return articles;
    }
}
