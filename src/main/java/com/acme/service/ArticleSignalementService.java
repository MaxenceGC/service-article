package com.acme.service;

import com.acme.controller.dto.request.ArticleSignalementRequest;
import com.acme.entity.article.Article;
import com.acme.entity.article.ArticleSignalement;
import com.acme.entity.article.enums.ArticleStatut;
import com.acme.entity.article.enums.signalement.GraviteType;
import com.acme.repository.ArticleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

import java.time.OffsetDateTime;
import java.util.UUID;

@ApplicationScoped
public class ArticleSignalementService {

    @Inject
    EntityManager em;

    @Inject
    ArticleRepository articleRepo;

    @Transactional
    public void signaler(UUID articleId, UUID signalantId, ArticleSignalementRequest req) {

        Article article = articleRepo.findById(articleId);
        if (article == null) {
            throw new WebApplicationException("Article not found", 404);
        }

        ArticleSignalement signalement = new ArticleSignalement();
        signalement.article = article;
        signalement.signalant_id = signalantId;
        signalement.raison = req.raison;
        signalement.niveau_gravite = req.niveauGravite;

        em.persist(signalement);

        article.nb_signalements++;
        article.date_mise_a_jour = OffsetDateTime.now();

        // Règle V1 : gravité critique => bannissement immédiat
        if (req.niveauGravite == GraviteType.critique) {
            article.statut = ArticleStatut.banni;
        }
    }
}
