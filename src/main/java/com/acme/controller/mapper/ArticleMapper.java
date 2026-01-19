package com.acme.controller.mapper;

import com.acme.controller.dto.response.ArticleResponse;
import com.acme.entity.article.Article;

public class ArticleMapper {

    public static ArticleResponse toResponse(Article a) {
        if (a == null) return null;

        ArticleResponse r = new ArticleResponse();
        r.id_article = a.id_article;
        r.titre = a.titre;
        r.description = a.description;
        r.prix_actuel = a.prix_actuel;
        r.statut = a.statut;
        r.date_creation = a.date_creation;
        r.date_mise_a_jour = a.date_mise_a_jour;

        if (a.boutique != null) {
            r.boutique = BoutiqueMapper.toResponse(a.boutique);
        }

        if (a.categorie != null) {
            r.categorie = CategorieMapper.toResponse(a.categorie);
        }

        return r;
    }
}




