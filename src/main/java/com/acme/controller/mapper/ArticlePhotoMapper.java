package com.acme.controller.mapper;

import com.acme.controller.dto.response.ArticlePhotoResponse;
import com.acme.entity.article.ArticlePhoto;

public class ArticlePhotoMapper {
    public static ArticlePhotoResponse toResponse(ArticlePhoto photo) {
        if (photo == null) {
            return null;
        }
        ArticlePhotoResponse response = new ArticlePhotoResponse();
        response.url = photo.url;
        response.ordre = photo.ordre;
        response.date_upload = photo.date_upload;
        return response;
    }
}

