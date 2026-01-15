package com.acme.repository;

import com.acme.entity.article.ArticlePhoto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ArticlePhotoRepository implements PanacheRepository<ArticlePhoto> {

    public ArticlePhoto findById(UUID id) {
        return find("id_photo", id).firstResult();
    }

    public List<ArticlePhoto> findByArticle(UUID articleId) {
        return find("article.id_article", articleId)
                .list();
    }

    public List<ArticlePhoto> findByArticleOrdered(UUID articleId) {
        return find(
                "article.id_article = ?1 order by ordre asc",
                articleId
        ).list();
    }

    public boolean existsForArticle(UUID articleId) {
        return count("article.id_article", articleId) > 0;
    }

    public long countByArticle(UUID articleId) {
        return count("article.id_article", articleId);
    }

    public void deleteById(UUID photoId) {
        delete("id_photo", photoId);
    }

    public void deleteByArticle(UUID articleId) {
        delete("article.id_article", articleId);
    }
}
