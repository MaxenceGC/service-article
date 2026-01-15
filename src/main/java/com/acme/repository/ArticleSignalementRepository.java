package com.acme.repository;

import com.acme.entity.article.ArticleSignalement;
import com.acme.entity.article.enums.signalement.GraviteType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ArticleSignalementRepository implements PanacheRepository<ArticleSignalement> {


    public ArticleSignalement findById(UUID id) {
        return find("id_signalement", id).firstResult();
    }

    public List<ArticleSignalement> findByArticle(UUID articleId) {
        return find("article.id_article", articleId)
                .list();
    }

    public List<ArticleSignalement> findByArticleAndGravite(UUID articleId, GraviteType gravite) {
        return find(
                "article.id_article = ?1 and niveau_gravite = ?2",
                articleId,
                gravite
        ).list();
    }

    public long countByArticle(UUID articleId) {
        return count("article.id_article", articleId);
    }

    public long countByArticleAndGravite(UUID articleId, GraviteType gravite) {
        return count(
                "article.id_article = ?1 and niveau_gravite = ?2",
                articleId,
                gravite
        );
    }

    public boolean hasCriticalSignalement(UUID articleId) {
        return count(
                "article.id_article = ?1 and niveau_gravite = 'critique'",
                articleId
        ) > 0;
    }


    public void deleteById(UUID id) {
        delete("id_signalement", id);
    }

    public void deleteByArticle(UUID articleId) {
        delete("article.id_article", articleId);
    }
}
