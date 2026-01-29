package com.acme.controller;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@RegisterForReflection
public class StaticResourceController {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response index() {
        return Response.ok(getResource("/index.html"))
                .header("Content-Type", "text/html; charset=utf-8")
                .build();
    }

    @GET
    @Path("{path: .*}")
    @Produces(MediaType.TEXT_HTML)
    public Response catchAll() {
        return Response.ok(getResource("/index.html"))
                .header("Content-Type", "text/html; charset=utf-8")
                .build();
    }

    private String getResource(String path) {
        try {
            return new String(this.getClass().getResourceAsStream(path).readAllBytes());
        } catch (Exception e) {
            return "<h1>404 - Not Found</h1>";
        }
    }
}
