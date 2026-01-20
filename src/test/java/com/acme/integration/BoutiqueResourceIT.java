package com.acme.integration;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class BoutiqueResourceIT extends IntegrationTestBase {

    @Test
    void createGetAndListBoutique() {
        UUID vendeurId = UUID.randomUUID();

        Map<String, Object> req = new HashMap<>();
        req.put("vendeur_id", vendeurId);
        req.put("nom", "Ma Boutique");
        req.put("description", "Desc");

        String boutiqueId =
                given()
                        .contentType(ContentType.JSON)
                        .body(req)
                .when()
                        .post("/boutiques")
                .then()
                        .statusCode(201)
                        .body("id_boutique", notNullValue())
                        .extract()
                        .path("id_boutique");

        given()
                .when()
                        .get("/boutiques/{id}", boutiqueId)
                .then()
                        .statusCode(200)
                        .body("nom", equalTo("Ma Boutique"));

        given()
                .queryParam("vendeurId", vendeurId)
                .when()
                        .get("/boutiques")
                .then()
                        .statusCode(200)
                        .body("$", hasSize(1));
    }
}
