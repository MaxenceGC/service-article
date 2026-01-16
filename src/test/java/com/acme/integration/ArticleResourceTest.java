package com.acme.integration;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class ArticleResourceTest extends IntegrationTestBase {

    @Test
    void createUpdateAndDeleteArticle() {
        UUID vendeurId = UUID.randomUUID();
        String categorieId = createCategorie("Cat 1");
        String newCategorieId = createCategorie("Cat 2");
        String boutiqueId = createBoutique(vendeurId);

        Map<String, Object> createReq = new HashMap<>();
        createReq.put("vendeur_id", vendeurId);
        createReq.put("categorie_id", categorieId);
        createReq.put("boutique_id", boutiqueId);
        createReq.put("titre", "Titre");
        createReq.put("description", "Desc");
        createReq.put("prix_actuel", 12.50);

        String articleId =
                given()
                        .contentType(ContentType.JSON)
                        .body(createReq)
                .when()
                        .post("/articles")
                .then()
                        .statusCode(201)
                        .body("id_article", notNullValue())
                        .body("statut", equalTo("en_attente"))
                        .extract()
                        .path("id_article");

        given()
                .when()
                        .get("/articles/{id}", articleId)
                .then()
                        .statusCode(200)
                        .body("id_article", equalTo(articleId));

        Map<String, Object> updateReq = new HashMap<>();
        updateReq.put("titre", "Titre mis a jour");
        updateReq.put("description", "Nouvelle desc");
        updateReq.put("categorieId", newCategorieId);

        given()
                .contentType(ContentType.JSON)
                .body(updateReq)
                .when()
                        .put("/articles/{id}", articleId)
                .then()
                        .statusCode(200)
                        .body("titre", equalTo("Titre mis a jour"))
                        .body("categorie.id_categorie", equalTo(newCategorieId));

        Map<String, Object> prixReq = new HashMap<>();
        prixReq.put("nouveauPrix", 15.00);

        given()
                .contentType(ContentType.JSON)
                .body(prixReq)
                .when()
                        .patch("/articles/{id}/prix", articleId)
                .then()
                        .statusCode(204);

        given()
                .queryParam("categorieId", newCategorieId)
                .when()
                        .get("/articles")
                .then()
                        .statusCode(200)
                        .body("size()", equalTo(1));

        given()
                .when()
                        .delete("/articles/{id}", articleId)
                .then()
                        .statusCode(204);

        given()
                .when()
                        .get("/articles/{id}", articleId)
                .then()
                        .statusCode(404);
    }

    private String createCategorie(String libelle) {
        Map<String, Object> req = new HashMap<>();
        req.put("libelle", libelle);
        req.put("description", "Desc " + libelle);

        return given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                        .post("/categories")
                .then()
                        .statusCode(201)
                        .extract()
                        .path("id_categorie");
    }

    private String createBoutique(UUID vendeurId) {
        Map<String, Object> req = new HashMap<>();
        req.put("vendeur_id", vendeurId);
        req.put("nom", "Boutique");
        req.put("description", "Desc");

        return given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                        .post("/boutiques")
                .then()
                        .statusCode(201)
                        .extract()
                        .path("id_boutique");
    }
}
