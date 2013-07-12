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

@Provider
public class AuthInterceptor implements ContainerRequestFilter, ContainerResponseFilter {

    @Inject
    TokenManager tokenMgr;

    @Context
    private UriInfo uriInfo;

    private boolean isAuthRequired() {
        if ("/secure/signin/validate".equals(uriInfo.getPath()))
            return false;
        return "secure".equals(uriInfo.getPathSegments().get(0).getPath());
    }

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        String token = context.getHeaders().getFirst("client_token");

        if (token != null) {
            tokenMgr.associate(token);
        }

        if (TokenManager.getUser() == null && isAuthRequired()) {
            context.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        tokenMgr.dissociate();
    }
}
