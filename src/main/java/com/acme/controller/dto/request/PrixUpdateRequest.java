package com.acme.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class PrixUpdateRequest {

    @NotNull
    @Positive
    public BigDecimal nouveauPrix;
}
