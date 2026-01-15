package com.acme.controller.dto.request;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public class CategorieRequest {

    @NotNull
    public String libelle;

    public String description;

    public UUID parent_id;
}

