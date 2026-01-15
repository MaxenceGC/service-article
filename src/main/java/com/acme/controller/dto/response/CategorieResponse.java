package com.acme.controller.dto.response;

import java.util.UUID;

public class CategorieResponse {
    public UUID id_categorie;
    public String libelle;
    public String description;
    public UUID parent_id;

    public CategorieResponse() {}

    public CategorieResponse(UUID id_categorie, String libelle, String description, UUID parent_id) {
        this.id_categorie = id_categorie;
        this.libelle = libelle;
        this.description = description;
        this.parent_id = parent_id;
    }
}

