package com.acme.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ArticleUpdateRequest {

    @NotNull
    public UUID id_article;

    public String titre;

    public String description;

    public UUID categorieId;

    public UUID boutique_id;
}
