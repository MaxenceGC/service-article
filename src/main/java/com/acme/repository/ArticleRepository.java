package com.acme.repository;

import com.acme.entity.article.Article;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import com.acme.entity.article.Article;
import com.acme.entity.article.enums.ArticleStatut;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.list;

@ApplicationScoped
public class ArticleRepository implements PanacheRepository<Article> {

    @Inject
    EntityManager em;

    public void persist(Article a) {
        em.persist(a);
    }

    public Article findById(UUID id) {
        return em.find(Article.class, id);
    }

    public List<Article> findAllArticles() {
        return listAll();
    }

    public List<Article> findByCategorie(UUID categorieId) {
        return list("categorie.id_categorie", categorieId);
    }

    public List<Article> findByVendeur(UUID vendeurId) {
        return list("vendeur_id", vendeurId);
    }

    public List<Article> findByStatut(ArticleStatut statut) {
        return list("statut", statut);
    }

    public List<Article> findByBoutique(UUID boutiqueId) {
        return list("boutique.id_boutique", boutiqueId);
    }
}
