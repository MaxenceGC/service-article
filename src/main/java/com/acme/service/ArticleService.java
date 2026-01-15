package com.acme.service;

import com.acme.controller.dto.request.ArticleRequest;
import com.acme.controller.dto.request.ArticleSignalementRequest;
import com.acme.controller.dto.request.ArticleUpdateRequest;
import com.acme.entity.article.Article;
import com.acme.entity.article.ArticleSignalement;
import com.acme.entity.article.PrixHistorique;
import com.acme.entity.article.enums.ArticleStatut;
import com.acme.entity.article.enums.prix.PrixOrigineType;
import com.acme.entity.article.enums.signalement.GraviteType;
import com.acme.entity.boutique.Boutique;
import com.acme.entity.categorie.Categorie;
import com.acme.repository.ArticleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ArticleService {

    @Inject
    EntityManager em;

    @Inject
    ArticleRepository repo;

    @Transactional
    public Article create(ArticleRequest req) {

        Categorie categorie = em.find(Categorie.class, req.categorie_id);
        if (categorie == null) {
            throw new WebApplicationException("Categorie not found", 400);
        }

        Boutique boutique = null;
        if (req.boutique_id != null) {
            boutique = em.find(Boutique.class, req.boutique_id);
            if (boutique == null) {
                throw new WebApplicationException("Boutique not found", 400);
            }
        }

        Article a = new Article();
        a.vendeur_id = req.vendeur_id != null ? req.vendeur_id : UUID.randomUUID();
        a.categorie = categorie;
        a.boutique = boutique;
        a.titre = req.titre;
        a.description = req.description;
        a.prix_actuel = req.prix_actuel;
        a.statut = ArticleStatut.en_attente;
        a.date_creation = OffsetDateTime.now();
        a.date_mise_a_jour = OffsetDateTime.now();

        repo.persist(a);
        return a;
    }

    public Article findById(UUID id) {
        return repo.findById(id);
    }

    public List<Article> list(UUID categorieId, UUID vendeurId, ArticleStatut statut) {
        if (categorieId != null) {
            return repo.findByCategorie(categorieId);
        }
        if (vendeurId != null) {
            return repo.findByVendeur(vendeurId);
        }
        if (statut != null) {
            return repo.findByStatut(statut);
        }
        return repo.listAll();
    }

    @Transactional
    public Article update(UUID id, ArticleUpdateRequest req) {
        Article article = repo.findById(id);
        if (article == null) {
            throw new WebApplicationException("Article not found", 404);
        }

        if (article.statut == ArticleStatut.vendu || article.statut == ArticleStatut.banni) {
            throw new WebApplicationException("Article not modifiable", 409);
        }

        Categorie categorie = em.find(Categorie.class, req.categorieId);
        if (categorie == null) {
            throw new WebApplicationException("Categorie not found", 400);
        }

        article.titre = req.titre;
        article.description = req.description;
        article.categorie = categorie;
        article.date_mise_a_jour = OffsetDateTime.now();

        return article;
    }

    @Transactional
    public void changePrix(UUID id, BigDecimal nouveauPrix) {
        Article article = repo.findById(id);
        if (article == null) {
            throw new WebApplicationException("Article not found", 404);
        }

        if (nouveauPrix.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WebApplicationException("Invalid price", 400);
        }

        if (article.prix_actuel.compareTo(nouveauPrix) == 0) {
            return;
        }

        PrixHistorique histo = new PrixHistorique();
        histo.article = article;
        histo.ancien_prix = article.prix_actuel;
        histo.nouveau_prix = nouveauPrix;
        histo.origine_changement = PrixOrigineType.vendeur;

        em.persist(histo);

        article.prix_actuel = nouveauPrix;
        article.date_mise_a_jour = OffsetDateTime.now();
    }

    @Transactional
    public void validate(UUID id) {
        Article article = repo.findById(id);
        if (article == null) {
            throw new WebApplicationException("Article not found", 404);
        }

        if (article.statut != ArticleStatut.en_attente) {
            throw new WebApplicationException("Invalid status transition", 409);
        }

        article.statut = ArticleStatut.valide;
        article.date_mise_a_jour = OffsetDateTime.now();
    }

    @Transactional
    public void refuse(UUID id, String motif) {
        Article article = repo.findById(id);
        if (article == null) {
            throw new WebApplicationException("Article not found", 404);
        }

        if (article.statut != ArticleStatut.en_attente) {
            throw new WebApplicationException("Invalid status transition", 409);
        }

        article.statut = ArticleStatut.refuse;
        article.motif_refus = motif;
        article.date_mise_a_jour = OffsetDateTime.now();
    }

    @Transactional
    public void delete(UUID id) {
        Article article = repo.findById(id);
        if (article == null) {
            throw new WebApplicationException("Article not found", 404);
        }

        repo.delete(article);
    }

    @Transactional
    public void signaler(
            UUID articleId,
            UUID signalantId,
            ArticleSignalementRequest req
    ) {
        Article article = repo.findById(articleId);
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

        if (req.niveauGravite == GraviteType.critique) {
            article.statut = ArticleStatut.banni;
        }

        article.date_mise_a_jour = OffsetDateTime.now();
    }

    @Transactional
    public void marquerVendu(UUID id) {
        Article article = repo.findById(id);
        if (article == null) {
            throw new WebApplicationException("Article not found", 404);
        }

        if (article.statut != ArticleStatut.valide) {
            throw new WebApplicationException("Article not sellable", 409);
        }

        article.statut = ArticleStatut.vendu;
        article.date_mise_a_jour = OffsetDateTime.now();
    }


}
