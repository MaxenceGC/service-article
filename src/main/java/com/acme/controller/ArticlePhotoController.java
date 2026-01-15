package com.acme.controller;

import com.acme.controller.dto.request.ArticlePhotoRequest;
import com.acme.entity.article.ArticlePhoto;
import com.acme.service.ArticlePhotoService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/articles/{articleId}/photos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArticlePhotoController {

    @Inject
    ArticlePhotoService photoService;

    @POST
    public Response addPhoto(@PathParam("articleId") UUID articleId, @Valid ArticlePhotoRequest request) {
        ArticlePhoto photo = photoService.add(articleId, request);
        return Response.status(Response.Status.CREATED).entity(photo).build();
    }

    @GET
    public Response listPhotos(@PathParam("articleId") UUID articleId) {
        List<ArticlePhoto> photos = photoService.list(articleId);
        return Response.ok(photos).build();
    }

    @DELETE
    @Path("/{photoId}")
    public Response deletePhoto(@PathParam("articleId") UUID articleId, @PathParam("photoId") UUID photoId) {
        photoService.delete(articleId, photoId);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{photoId}/ordre")
    public Response updateOrdre(@PathParam("photoId") UUID photoId, @QueryParam("ordre") int ordre) {
        if (ordre < 0) {
            throw new BadRequestException("L'ordre doit être >= 0");
        }

        photoService.updateOrdre(photoId, ordre);
        return Response.noContent().build();
    }
}
