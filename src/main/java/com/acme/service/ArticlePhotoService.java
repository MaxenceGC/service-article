package com.acme.service;

import com.acme.controller.dto.request.ArticlePhotoRequest;
import com.acme.entity.article.Article;
import com.acme.entity.article.ArticlePhoto;
import com.acme.entity.article.enums.ArticleStatut;
import com.acme.repository.ArticlePhotoRepository;
import com.acme.repository.ArticleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ArticlePhotoService {

    @Inject
    EntityManager em;

    @Inject
    ArticleRepository articleRepo;

    // Si tu as un repo dédié
    @Inject
    ArticlePhotoRepository photoRepo;

    @Transactional
    public ArticlePhoto add(UUID articleId, ArticlePhotoRequest req) {

        Article article = articleRepo.findById(articleId);
        if (article == null) {
            throw new WebApplicationException("Article not found", 404);
        }

        if (article.statut == ArticleStatut.vendu || article.statut == ArticleStatut.banni) {
            throw new WebApplicationException("Cannot add photo to this article", 409);
        }

        ArticlePhoto photo = new ArticlePhoto();
        photo.article = article;
        photo.url = req.url;
        photo.ordre = req.ordre;

        em.persist(photo);

        article.date_mise_a_jour = OffsetDateTime.now();

        return photo;
    }

    public List<ArticlePhoto> list(UUID articleId) {

        Article article = articleRepo.findById(articleId);
        if (article == null) {
            throw new WebApplicationException("Article not found", 404);
        }

        return photoRepo.findByArticle(articleId);
    }

    @Transactional
    public void delete(UUID articleId, UUID photoId) {

        ArticlePhoto photo = em.find(ArticlePhoto.class, photoId);
        if (photo == null) {
            throw new WebApplicationException("Photo not found", 404);
        }

        if (!photo.article.id_article.equals(articleId)) {
            throw new WebApplicationException("Photo does not belong to article", 409);
        }

        em.remove(photo);

        photo.article.date_mise_a_jour = OffsetDateTime.now();
    }

    @Transactional
    public void updateOrdre(UUID photoId, int ordre) {

        if (ordre < 0) {
            throw new WebApplicationException("Invalid order", 400);
        }

        ArticlePhoto photo = em.find(ArticlePhoto.class, photoId);
        if (photo == null) {
            throw new WebApplicationException("Photo not found", 404);
        }

        photo.ordre = ordre;
        photo.article.date_mise_a_jour = OffsetDateTime.now();
    }
}
