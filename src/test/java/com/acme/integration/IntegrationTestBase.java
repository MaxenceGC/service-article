package com.acme.integration;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;

abstract class IntegrationTestBase {

    @Inject
    EntityManager em;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        em.createNativeQuery("DELETE FROM article_signalement").executeUpdate();
        em.createNativeQuery("DELETE FROM article_photo").executeUpdate();
        em.createNativeQuery("DELETE FROM prix_historique").executeUpdate();
        em.createNativeQuery("DELETE FROM article").executeUpdate();
        em.createNativeQuery("DELETE FROM boutique").executeUpdate();
        em.createNativeQuery("DELETE FROM categorie").executeUpdate();
    }
}
