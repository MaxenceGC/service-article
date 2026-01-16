package com.acme.integration;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class CategorieResourceTest extends IntegrationTestBase {

    @Test
    void createListChildrenAndTree() {
        String parentId = createCategorie("Parent", null);
        String childId = createCategorie("Enfant", parentId);

        given()
                .queryParam("parentId", parentId)
                .when()
                        .get("/categories")
                .then()
                        .statusCode(200)
                        .body("$", hasSize(1))
                        .body("[0].id_categorie", equalTo(childId));

        given()
                .when()
                        .get("/categories/{id}/children", parentId)
                .then()
                        .statusCode(200)
                        .body("$", hasSize(1))
                        .body("[0].id_categorie", equalTo(childId));

        given()
                .when()
                        .get("/categories/tree")
                .then()
                        .statusCode(200)
                        .body("$", hasSize(1))
                        .body("[0].id", equalTo(parentId))
                        .body("[0].children", hasSize(1))
                        .body("[0].children[0].id", equalTo(childId));
    }

    private String createCategorie(String libelle, String parentId) {
        Map<String, Object> req = new HashMap<>();
        req.put("libelle", libelle);
        req.put("description", "Desc " + libelle);
        if (parentId != null) {
            req.put("parent_id", parentId);
        }

        return given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                        .post("/categories")
                .then()
                        .statusCode(201)
                        .body("id_categorie", notNullValue())
                        .extract()
                        .path("id_categorie");
    }
}
