package com.acme.repository;

import com.acme.entity.categorie.Categorie;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CategorieRepository {

    @Inject
    EntityManager em;

    public void persist(Categorie c) {
        em.persist(c);
    }

    public Categorie findById(UUID id) {
        return em.find(Categorie.class, id);
    }

    public Categorie update(Categorie c) {
        return em.merge(c);
    }

    public void delete(Categorie c) {
        Categorie managed = em.contains(c) ? c : em.merge(c);
        em.remove(managed);
    }

    public List<Categorie> listAll() {
        return em.createQuery("FROM Categorie", Categorie.class).getResultList();
    }

    public <T> T getReference(Class<T> clazz, UUID id) {
        if (id == null) return null;
        return em.getReference(clazz, id);
    }
}
