package com.acme.controller.dto.request;

import com.acme.entity.article.enums.ArticleStatut;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public class ArticleRequest {

    public UUID vendeur_id;

    public UUID boutique_id;

    @NotNull
    public UUID categorie_id;

    @NotBlank
    @Size(max = 255)
    public String titre;

    @Size(max = 2000)
    public String description;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    public BigDecimal prix_actuel;

    public ArticleStatut statut;

}
