package com.acme.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ArticleUpdateRequest {

    public UUID id_article;

    @NotBlank
    public String titre;

    public String description;

    @NotNull
    public UUID categorieId;
}
