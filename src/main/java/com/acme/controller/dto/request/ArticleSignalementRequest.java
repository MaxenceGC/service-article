package com.acme.controller.dto.request;

import com.acme.entity.article.enums.signalement.GraviteType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ArticleSignalementRequest {

    public UUID signalantId;

    @NotBlank
    public String raison;

    @NotNull
    public GraviteType niveauGravite;

}
