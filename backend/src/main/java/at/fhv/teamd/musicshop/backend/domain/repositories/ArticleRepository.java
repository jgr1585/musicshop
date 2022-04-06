package at.fhv.teamd.musicshop.backend.domain.repositories;

import at.fhv.teamd.musicshop.backend.domain.article.Article;

import java.util.Optional;
import java.util.SortedSet;

public interface ArticleRepository {
    SortedSet<Article> searchArticlesByAttributes(String title, String artist);

    Optional<Article> findArticleById(Long id);
}
