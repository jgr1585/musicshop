package at.fhv.teamd.musicshop.backend.domain.repositories;

import at.fhv.teamd.musicshop.backend.domain.article.Article;

import java.util.Optional;
import java.util.Set;

public interface ArticleRepository {
    Set<Article> searchArticlesByAttributes(String title, String artist);

    Optional<Article> findArticlesById(Long id);
}
