package com.acme.controller.mapper;

import com.acme.controller.dto.response.BoutiqueResponse;
import com.acme.entity.boutique.Boutique;

public class BoutiqueMapper {
    public static BoutiqueResponse toResponse(Boutique boutique) {
        if (boutique == null) {
            return null;
        }
        BoutiqueResponse response = new BoutiqueResponse();
        response.id_boutique = boutique.id_boutique;
        response.nom = boutique.nom;
        response.description = boutique.description;
        response.date_creation = boutique.date_creation;
        return response;
    }
}

