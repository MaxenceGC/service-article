package com.acme.service;

import com.acme.controller.dto.request.BoutiqueRequest;
import com.acme.entity.boutique.Boutique;
import com.acme.repository.BoutiqueRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BoutiqueService {

    @Inject
    BoutiqueRepository repo;

    @Transactional
    public Boutique create(BoutiqueRequest req) {
        Boutique b = new Boutique();
        b.vendeur_id = req.vendeur_id != null ? req.vendeur_id : UUID.randomUUID();
        b.nom = req.nom;
        b.description = req.description;
        b.date_creation = OffsetDateTime.now();
        repo.persist(b);
        return b;
    }

    public Boutique findById(UUID id) {
        return repo.findById(id);
    }

    public List<Boutique> listAll() {
        return repo.listAll();
    }

    @Transactional
    public Boutique update(UUID id, BoutiqueRequest req) {
        Boutique b = repo.findById(id);
        if (b == null) return null;
        b.nom = req.nom != null ? req.nom : b.nom;
        b.description = req.description != null ? req.description : b.description;
        return repo.update(b);
    }

    @Transactional
    public boolean delete(UUID id) {
        Boutique b = repo.findById(id);
        if (b == null) return false;
        repo.delete(b);
        return true;
    }
}
