package com.acme.controller;

import com.acme.controller.dto.request.ArticleSignalementRequest;
import com.acme.service.ArticleSignalementService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/articles/{articleId}/signalements")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ArticleSignalementController {

    @Inject
    ArticleSignalementService articleSignalementService;

    @POST
    public Response signaler(@PathParam("articleId") UUID articleId, @Valid ArticleSignalementRequest request) {
        UUID signalantId = request.signalantId != null
                ? request.signalantId
                : UUID.randomUUID();

        articleSignalementService.signaler(
                articleId,
                signalantId,
                request
        );

        return Response.status(Response.Status.CREATED).build();
    }
}
