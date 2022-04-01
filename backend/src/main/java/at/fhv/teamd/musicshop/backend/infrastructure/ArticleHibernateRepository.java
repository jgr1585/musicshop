package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public List<Article> searchArticlesByAttributes(String title, String artist) {
        Objects.requireNonNull(title);
        Objects.requireNonNull(artist);

        EntityManager em = PersistenceManager.getEntityManagerInstance();

        // NOTE: This redundant querying is done because abstract article cannot be directly queried
        // because the required JPA inheritance strategy probably cannot be used
        TypedQuery<Album> albumQuery = em.createQuery(
                "SELECT DISTINCT(a) FROM Album a JOIN a.artists aa WHERE " +
                        "((a.title <> '' AND LOWER(a.title) LIKE LOWER(:titlePattern)) OR a.title = '') AND " +
                        "((aa.name <> '' AND LOWER(aa.name) LIKE LOWER(:artistPattern)) OR aa.name = '')"
                , Album.class).setMaxResults(25);

        albumQuery.setParameter("titlePattern", "%"+title+"%");
        albumQuery.setParameter("artistPattern", "%"+artist+"%");

        TypedQuery<Song> songQuery = em.createQuery("SELECT DISTINCT(s) FROM Song s JOIN s.artists sa WHERE " +
                        "((s.title <> '' AND LOWER(s.title) LIKE LOWER(:titlePattern)) OR s.title = '') AND " +
                        "((sa.name <> '' AND LOWER(sa.name) LIKE LOWER(:artistPattern)) OR sa.name = '')"
                , Song.class).setMaxResults(25);

        songQuery.setParameter("titlePattern", "%"+title+"%");
        songQuery.setParameter("artistPattern", "%"+artist+"%");

        List<Article> articles = new ArrayList<>();

        // NOTE: Alternative to this is to order by case select in query
        List<Album> albums = albumQuery.getResultList();
        List<Album> albumsDirectMatches = albums.stream()
                .filter(album -> album.getTitle().equals(title)
                                || album.getArtists().stream().anyMatch(
                                        albumArtist -> albumArtist.getName().equals(artist)
                                    )
                )
                .collect(Collectors.toList());
        List<Album> albumsIndirectMatches = albums.stream()
                .filter(album -> !albumsDirectMatches.contains(album))
                .collect(Collectors.toList());

        List<Song> songs = songQuery.getResultList();
        List<Song> songsDirectMatches = songs.stream()
                .filter(song -> song.getTitle().equals(title)
                                || song.getArtists().stream().anyMatch(
                                songArtist -> songArtist.getName().equals(artist)
                        )
                )
                .collect(Collectors.toList());
        List<Song> songsIndirectMatches = songs.stream()
                .filter(song -> !songsDirectMatches.contains(song))
                .collect(Collectors.toList());

        articles.addAll(albumsDirectMatches);
        articles.addAll(songsDirectMatches);
        articles.addAll(albumsIndirectMatches);
        articles.addAll(songsIndirectMatches);

        em.close();

        return articles;
    }
}
