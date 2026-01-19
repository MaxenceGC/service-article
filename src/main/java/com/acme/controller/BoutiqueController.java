package com.acme.controller;

import com.acme.controller.dto.request.BoutiqueRequest;
import com.acme.controller.dto.response.BoutiqueResponse;
import com.acme.controller.mapper.BoutiqueMapper;
import com.acme.entity.boutique.Boutique;
import com.acme.service.BoutiqueService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/boutiques")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoutiqueController {

    @Inject
    BoutiqueService boutiqueService;

    @POST
    public Response create(@Valid BoutiqueRequest req) {
        Boutique b = boutiqueService.create(req);
        return Response.status(Response.Status.CREATED).entity(BoutiqueMapper.toResponse(b)).build();
    }

    @GET
    public Response list(@QueryParam("vendeurId") UUID vendeurId) {
        List<Boutique> list = boutiqueService.listAll();
        if (vendeurId != null) {
            List<BoutiqueResponse> filtered = list.stream()
                    .filter(b -> b.vendeur_id != null && vendeurId.equals(b.vendeur_id))
                    .map(BoutiqueMapper::toResponse)
                    .collect(Collectors.toList());
            return Response.ok(filtered).build();
        }
        return Response.ok(list.stream().map(BoutiqueMapper::toResponse).collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") UUID id) {
        Boutique b = boutiqueService.findById(id);
        if (b == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(BoutiqueMapper.toResponse(b)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, BoutiqueRequest req) {
        Boutique b = boutiqueService.update(id, req);
        if (b == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(BoutiqueMapper.toResponse(b)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        boolean deleted = boutiqueService.delete(id);
        if (!deleted) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.noContent().build();
    }
}

