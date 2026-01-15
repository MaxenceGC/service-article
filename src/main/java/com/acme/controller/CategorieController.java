package com.acme.controller;

import com.acme.controller.dto.request.CategorieRequest;
import com.acme.controller.dto.response.CategorieResponse;
import com.acme.controller.mapper.CategorieMapper;
import com.acme.entity.categorie.Categorie;
import com.acme.service.CategorieService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;
import java.util.stream.Collectors;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategorieController {

    @Inject
    CategorieService categorieService;

    @POST
    public Response create(@Valid CategorieRequest req) {
        // basic check: parent can't be same as provided (no self-parent on create can't be enforced here)
        Categorie c = categorieService.create(req);
        return Response.status(Response.Status.CREATED).entity(CategorieMapper.toResponse(c)).build();
    }

    @GET
    public Response list(@QueryParam("parentId") UUID parentId) {
        List<Categorie> list = categorieService.listAll();
        if (parentId != null) {
            List<CategorieResponse> filtered = list.stream()
                    .filter(cat -> cat.parent != null && parentId.equals(cat.parent.id_categorie))
                    .map(CategorieMapper::toResponse)
                    .collect(Collectors.toList());
            return Response.ok(filtered).build();
        }
        return Response.ok(list.stream().map(CategorieMapper::toResponse).collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") UUID id) {
        Categorie c = categorieService.findById(id);
        if (c == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(CategorieMapper.toResponse(c)).build();
    }

    @GET
    @Path("/{id}/children")
    public Response children(@PathParam("id") UUID id) {
        List<Categorie> list = categorieService.listAll();
        List<CategorieResponse> children = list.stream()
                .filter(cat -> cat.parent != null && id.equals(cat.parent.id_categorie))
                .map(CategorieMapper::toResponse)
                .collect(Collectors.toList());
        return Response.ok(children).build();
    }

    @GET
    @Path("/tree")
    public Response tree() {
        List<Categorie> all = categorieService.listAll();
        // map id -> node
        Map<UUID, CategoryNode> nodes = new HashMap<>();
        for (Categorie c : all) {
            nodes.put(c.id_categorie, new CategoryNode(c.id_categorie, c.libelle, c.description));
        }
        // build tree
        List<CategoryNode> roots = new ArrayList<>();
        for (Categorie c : all) {
            CategoryNode node = nodes.get(c.id_categorie);
            if (c.parent != null && c.parent.id_categorie != null) {
                CategoryNode parentNode = nodes.get(c.parent.id_categorie);
                if (parentNode != null) {
                    parentNode.children.add(node);
                } else {
                    roots.add(node);
                }
            } else {
                roots.add(node);
            }
        }
        return Response.ok(roots).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, CategorieRequest req) {
        // basic safeguard: don't allow setting parent to itself
        if (req.parent_id != null && req.parent_id.equals(id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("parent_id cannot be same as category id").build();
        }
        Categorie c = categorieService.update(id, req);
        if (c == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(CategorieMapper.toResponse(c)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        boolean deleted = categorieService.delete(id);
        if (!deleted) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.noContent().build();
    }

    // Simple DTO for tree responses
    public static class CategoryNode {
        public UUID id;
        public String libelle;
        public String description;
        public List<CategoryNode> children = new ArrayList<>();

        public CategoryNode(UUID id, String libelle, String description) {
            this.id = id;
            this.libelle = libelle;
            this.description = description;
        }
    }
}
