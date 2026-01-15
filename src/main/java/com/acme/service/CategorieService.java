package com.acme.service;

import com.acme.controller.dto.request.CategorieRequest;
import com.acme.entity.categorie.Categorie;
import com.acme.repository.CategorieRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CategorieService {

    @Inject
    CategorieRepository repo;

    @Transactional
    public Categorie create(CategorieRequest req) {
        Categorie c = new Categorie();
        c.libelle = req.libelle;
        c.description = req.description;
        if (req.parent_id != null) {
            c.parent = repo.getReference(Categorie.class, req.parent_id);
        }
        repo.persist(c);
        return c;
    }

    public Categorie findById(UUID id) {
        return repo.findById(id);
    }

    public List<Categorie> listAll() {
        return repo.listAll();
    }

    @Transactional
    public Categorie update(UUID id, CategorieRequest req) {
        Categorie c = repo.findById(id);
        if (c == null) return null;
        c.libelle = req.libelle != null ? req.libelle : c.libelle;
        c.description = req.description != null ? req.description : c.description;
        if (req.parent_id != null) {
            c.parent = repo.getReference(Categorie.class, req.parent_id);
        } else {
            c.parent = null;
        }
        return repo.update(c);
    }

    @Transactional
    public boolean delete(UUID id) {
        Categorie c = repo.findById(id);
        if (c == null) return false;
        repo.delete(c);
        return true;
    }
}
