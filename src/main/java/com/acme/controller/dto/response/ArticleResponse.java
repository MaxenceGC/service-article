package com.acme.controller.dto.response;

import com.acme.entity.article.enums.ArticleStatut;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ArticleResponse {
    public UUID id_article;
    public String titre;
    public String description;
    public BigDecimal prix_actuel;
    public ArticleStatut statut;
    public OffsetDateTime date_creation;
    public OffsetDateTime date_mise_a_jour;
    public BoutiqueResponse boutique;
    public CategorieResponse categorie;
}
