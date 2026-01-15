package com.acme.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ArticleRefusRequest {

    @NotBlank
    public String motif;
}
