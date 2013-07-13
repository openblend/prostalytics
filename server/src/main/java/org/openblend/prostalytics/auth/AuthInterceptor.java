package org.openblend.prostalytics.auth;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashSet;

@Provider
public class AuthInterceptor implements ContainerRequestFilter, ContainerResponseFilter {

    @Inject
    private AuthManager authManager;

    @Context
    private UriInfo uriInfo;

    private static HashSet<String> openResources = new HashSet<String>();

    static {
        openResources.add("/auth/register");
        openResources.add("/auth/login");
    }

    private boolean isAuthRequired() {
        if (openResources.contains(uriInfo.getPath())) {
            return false;
        }

        return true;
    }

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        String token = context.getHeaders().getFirst("client_token");

        if (token != null) {
            authManager.associate(token);
        }

        if (authManager.getUser() == null && isAuthRequired()) {
            context.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        authManager.dissociate();
    }
}
