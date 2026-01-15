package com.acme.repository;

import com.acme.entity.boutique.Boutique;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BoutiqueRepository {

    @Inject
    EntityManager em;

    public void persist(Boutique b) {
        em.persist(b);
    }

    public Boutique findById(UUID id) {
        return em.find(Boutique.class, id);
    }

    public Boutique update(Boutique b) {
        return em.merge(b);
    }

    public void delete(Boutique b) {
        Boutique managed = em.contains(b) ? b : em.merge(b);
        em.remove(managed);
    }

    public List<Boutique> listAll() {
        return em.createQuery("FROM Boutique", Boutique.class).getResultList();
    }

    public <T> T getReference(Class<T> clazz, UUID id) {
        if (id == null) return null;
        return em.getReference(clazz, id);
    }
}
