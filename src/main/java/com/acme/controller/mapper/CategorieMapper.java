package com.acme.controller.mapper;

import com.acme.controller.dto.response.CategorieResponse;
import com.acme.entity.categorie.Categorie;

public class CategorieMapper {
    public static CategorieResponse toResponse(Categorie categorie) {
        if (categorie == null) {
            return null;
        }
        return new CategorieResponse(
                categorie.id_categorie,
                categorie.libelle,
                categorie.description,
                categorie.parent != null ? categorie.parent.id_categorie : null
        );
    }
}

