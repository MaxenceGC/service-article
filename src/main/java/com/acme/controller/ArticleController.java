package com.acme.controller;

import com.acme.controller.dto.request.ArticleRefusRequest;
import com.acme.controller.dto.request.ArticleRequest;
import com.acme.controller.dto.request.ArticleUpdateRequest;
import com.acme.controller.dto.request.PrixUpdateRequest;
import com.acme.controller.mapper.ArticleMapper;
import com.acme.entity.article.Article;
import com.acme.entity.article.enums.ArticleStatut;
import com.acme.service.ArticleService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/articles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArticleController {

    @Inject
    ArticleService articleService;

    @POST
    public Response create(@Valid ArticleRequest req) {
        Article a = articleService.create(req);
        return Response
                .status(Response.Status.CREATED)
                .entity(ArticleMapper.toResponse(a))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") UUID id) {
        Article a = articleService.findById(id);
        if (a == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ArticleMapper.toResponse(a)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, @Valid ArticleUpdateRequest req) {
        return Response.ok(
                ArticleMapper.toResponse(articleService.update(id, req))
        ).build();
    }

    @GET
    public Response list(@QueryParam("categorieId") UUID categorieId, @QueryParam("vendeurId") UUID vendeurId, @QueryParam("statut") ArticleStatut statut) {
        List<Article> articles = articleService.list(categorieId, vendeurId, statut);
        return Response.ok(articles.stream().map(ArticleMapper::toResponse).collect(java.util.stream.Collectors.toList())).build();
    }

    @PATCH
    @Path("/{id}/prix")
    public Response updatePrix(@PathParam("id") UUID id, @Valid PrixUpdateRequest req) {
        articleService.changePrix(id, req.nouveauPrix);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/valider")
    public Response validate(@PathParam("id") UUID id) {
        articleService.validate(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/refuser")
    public Response refuse(@PathParam("id") UUID id, @Valid ArticleRefusRequest req) {
        articleService.refuse(id, req.motif);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        articleService.delete(id);
        return Response.noContent().build();
    }
}
