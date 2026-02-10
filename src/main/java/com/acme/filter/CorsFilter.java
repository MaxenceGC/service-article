package com.acme.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * CORS Filter pour permettre les requêtes cross-origin depuis le frontend
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        String origin = request.getHeaderString("Origin");

        // Autoriser localhost et le domaine de production
        if (origin != null && (origin.startsWith("http://localhost") ||
                               origin.startsWith("https://localhost") ||
                               origin.equals("https://satisfactorysquad.freeboxos.fr"))) {
            response.getHeaders().add("Access-Control-Allow-Origin", origin);
            response.getHeaders().add("Access-Control-Allow-Credentials", "true");
            response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
            response.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.getHeaders().add("Access-Control-Max-Age", "3600");
        }
    }
}
