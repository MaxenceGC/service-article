package com.acme.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ArticlePhotoRequest {

    @NotBlank
    public String url;

    public int ordre;
}
