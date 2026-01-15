package com.acme.controller.dto.request;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public class BoutiqueRequest {

    public UUID vendeur_id;

    @NotNull
    public String nom;

    public String description;
}

