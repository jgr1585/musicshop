package at.fhv.teamd.musicshop.backend.domain.repositories;

import at.fhv.teamd.musicshop.backend.domain.article.Article;

import java.util.List;

public interface ArticleRepository {
    List<Article> searchArticlesByAttributes(String title, String artist);
}
