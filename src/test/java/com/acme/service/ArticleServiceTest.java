package com.acme.service;

import com.acme.controller.dto.request.ArticleRequest;
import com.acme.controller.dto.request.ArticleSignalementRequest;
import com.acme.entity.article.Article;
import com.acme.entity.article.ArticleSignalement;
import com.acme.entity.article.PrixHistorique;
import com.acme.entity.article.enums.ArticleStatut;
import com.acme.entity.article.enums.prix.PrixOrigineType;
import com.acme.entity.article.enums.signalement.GraviteType;
import com.acme.entity.boutique.Boutique;
import com.acme.entity.categorie.Categorie;
import com.acme.repository.ArticleRepository;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    EntityManager em;

    @Mock
    ArticleRepository repo;

    ArticleService service;

    @BeforeEach
    void setUp() {
        service = new ArticleService();
        service.em = em;
        service.repo = repo;
    }

    @Test
    void create_throwsWhenCategorieMissing() {
        ArticleRequest req = new ArticleRequest();
        req.categorie_id = UUID.randomUUID();
        req.titre = "Titre";
        req.prix_actuel = BigDecimal.ONE;

        when(em.find(Categorie.class, req.categorie_id)).thenReturn(null);

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> service.create(req));
        assertEquals(400, ex.getResponse().getStatus());
        verifyNoInteractions(repo);
    }

    @Test
    void create_throwsWhenBoutiqueMissing() {
        ArticleRequest req = new ArticleRequest();
        req.categorie_id = UUID.randomUUID();
        req.boutique_id = UUID.randomUUID();
        req.titre = "Titre";
        req.prix_actuel = BigDecimal.ONE;

        when(em.find(Categorie.class, req.categorie_id)).thenReturn(new Categorie());
        when(em.find(Boutique.class, req.boutique_id)).thenReturn(null);

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> service.create(req));
        assertEquals(400, ex.getResponse().getStatus());
        verifyNoInteractions(repo);
    }

    @Test
    void create_persistsArticleAndDefaults() {
        ArticleRequest req = new ArticleRequest();
        req.categorie_id = UUID.randomUUID();
        req.vendeur_id = UUID.randomUUID();
        req.titre = "Titre";
        req.description = "Desc";
        req.prix_actuel = new BigDecimal("12.50");

        Categorie categorie = new Categorie();
        when(em.find(Categorie.class, req.categorie_id)).thenReturn(categorie);

        Article created = service.create(req);

        ArgumentCaptor<Article> captor = ArgumentCaptor.forClass(Article.class);
        verify(repo).persist(captor.capture());
        Article persisted = captor.getValue();

        assertEquals(req.vendeur_id, persisted.vendeur_id);
        assertEquals(categorie, persisted.categorie);
        assertNull(persisted.boutique);
        assertEquals(req.titre, persisted.titre);
        assertEquals(req.description, persisted.description);
        assertEquals(req.prix_actuel, persisted.prix_actuel);
        assertEquals(ArticleStatut.en_attente, persisted.statut);
        assertNotNull(persisted.date_creation);
        assertNotNull(persisted.date_mise_a_jour);
        assertEquals(persisted, created);
    }

    @Test
    void list_prefersCategorieFilter() {
        UUID categorieId = UUID.randomUUID();
        UUID vendeurId = UUID.randomUUID();

        when(repo.findByCategorie(categorieId)).thenReturn(List.of());

        assertEquals(0, service.list(categorieId, vendeurId, ArticleStatut.en_attente).size());
        verify(repo).findByCategorie(categorieId);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void update_throwsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(null);

        WebApplicationException ex = assertThrows(WebApplicationException.class, () ->
                service.update(id, new com.acme.controller.dto.request.ArticleUpdateRequest()));
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void update_throwsWhenNotModifiable() {
        UUID id = UUID.randomUUID();
        Article article = new Article();
        article.statut = ArticleStatut.vendu;

        when(repo.findById(id)).thenReturn(article);

        WebApplicationException ex = assertThrows(WebApplicationException.class, () ->
                service.update(id, new com.acme.controller.dto.request.ArticleUpdateRequest()));
        assertEquals(409, ex.getResponse().getStatus());
        verifyNoInteractions(em);
    }

    @Test
    void update_updatesFieldsAndCategorie() {
        UUID id = UUID.randomUUID();
        Article article = new Article();
        article.statut = ArticleStatut.en_attente;

        com.acme.controller.dto.request.ArticleUpdateRequest req =
                new com.acme.controller.dto.request.ArticleUpdateRequest();
        req.titre = "Nouveau titre";
        req.description = "Nouvelle desc";
        req.categorieId = UUID.randomUUID();

        Categorie categorie = new Categorie();
        when(repo.findById(id)).thenReturn(article);
        when(em.find(Categorie.class, req.categorieId)).thenReturn(categorie);

        Article updated = service.update(id, req);

        assertEquals("Nouveau titre", article.titre);
        assertEquals("Nouvelle desc", article.description);
        assertEquals(categorie, article.categorie);
        assertNotNull(article.date_mise_a_jour);
        assertEquals(article, updated);
    }

    @Test
    void update_throwsWhenCategorieMissing() {
        UUID id = UUID.randomUUID();
        Article article = new Article();
        article.statut = ArticleStatut.en_attente;

        com.acme.controller.dto.request.ArticleUpdateRequest req =
                new com.acme.controller.dto.request.ArticleUpdateRequest();
        req.titre = "Titre";
        req.categorieId = UUID.randomUUID();

        when(repo.findById(id)).thenReturn(article);
        when(em.find(Categorie.class, req.categorieId)).thenReturn(null);

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> service.update(id, req));
        assertEquals(400, ex.getResponse().getStatus());
    }

    @Test
    void changePrix_persistsHistoryAndUpdatesArticle() {
        UUID id = UUID.randomUUID();
        Article article = new Article();
        article.prix_actuel = new BigDecimal("10.00");

        when(repo.findById(id)).thenReturn(article);

        // Mock de la requête native et chaînage des paramètres
        jakarta.persistence.Query q = org.mockito.Mockito.mock(jakarta.persistence.Query.class);
        when(em.createNativeQuery(org.mockito.ArgumentMatchers.anyString())).thenReturn(q);
        when(q.setParameter(org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.any())).thenReturn(q);
        when(q.executeUpdate()).thenReturn(1);

        service.changePrix(id, new BigDecimal("12.00"));

        // Vérifier que la requête native a été exécutée
        verify(em).createNativeQuery(org.mockito.ArgumentMatchers.anyString());
        verify(q).executeUpdate();
        // Et que l'entité Article est mise à jour
        assertEquals(new BigDecimal("12.00"), article.prix_actuel);
        assertNotNull(article.date_mise_a_jour);
        // Aucune persistance de PrixHistorique via em.persist avec l'impl actuelle
        verify(em, never()).persist(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void changePrix_returnsWhenSamePrice() {
        UUID id = UUID.randomUUID();
        Article article = new Article();
        article.prix_actuel = new BigDecimal("10.00");

        when(repo.findById(id)).thenReturn(article);

        service.changePrix(id, new BigDecimal("10.00"));

        verify(em, never()).persist(any());
        // Vérifier qu'aucune requête native n'est déclenchée quand le prix est identique
        verify(em, never()).createNativeQuery(org.mockito.ArgumentMatchers.anyString());
        assertEquals(new BigDecimal("10.00"), article.prix_actuel);
        assertNull(article.date_mise_a_jour);
    }

    @Test
    void validate_updatesStatusWhenEnAttente() {
        UUID id = UUID.randomUUID();
        Article article = new Article();
        article.statut = ArticleStatut.en_attente;

        when(repo.findById(id)).thenReturn(article);

        service.validate(id);

        assertEquals(ArticleStatut.valide, article.statut);
        assertNotNull(article.date_mise_a_jour);
    }

    @Test
    void refuse_updatesStatusAndMotif() {
        UUID id = UUID.randomUUID();
        Article article = new Article();
        article.statut = ArticleStatut.en_attente;

        when(repo.findById(id)).thenReturn(article);

        service.refuse(id, "motif");

        assertEquals(ArticleStatut.refuse, article.statut);
        assertEquals("motif", article.motif_refus);
        assertNotNull(article.date_mise_a_jour);
    }

    @Test
    void delete_callsRepoDeleteWhenFound() {
        UUID id = UUID.randomUUID();
        Article article = new Article();

        when(repo.findById(id)).thenReturn(article);

        service.delete(id);

        verify(repo).delete(article);
    }

    @Test
    void signaler_persistsSignalementAndBansOnCritique() {
        UUID articleId = UUID.randomUUID();
        UUID signalantId = UUID.randomUUID();
        Article article = new Article();
        article.statut = ArticleStatut.en_attente;

        ArticleSignalementRequest req = new ArticleSignalementRequest();
        req.raison = "spam";
        req.niveauGravite = GraviteType.critique;

        when(repo.findById(articleId)).thenReturn(article);

        service.signaler(articleId, signalantId, req);

        ArgumentCaptor<ArticleSignalement> captor = ArgumentCaptor.forClass(ArticleSignalement.class);
        verify(em).persist(captor.capture());
        ArticleSignalement signalement = captor.getValue();

        assertEquals(article, signalement.article);
        assertEquals(signalantId, signalement.signalant_id);
        assertEquals("spam", signalement.raison);
        assertEquals(GraviteType.critique, signalement.niveau_gravite);
        assertEquals(1, article.nb_signalements);
        assertEquals(ArticleStatut.banni, article.statut);
        assertNotNull(article.date_mise_a_jour);
    }
}
