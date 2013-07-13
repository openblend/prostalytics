package org.openblend.prostalytics.auth;

import org.openblend.prostalytics.controller.Navigation;
import org.openblend.prostalytics.auth.dao.AuthDAO;
import org.openblend.prostalytics.auth.dao.UserDAO;
import org.openblend.prostalytics.auth.domain.User;

import java.net.URI;
import java.net.URISyntaxException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;


@Path("/auth")
@ApplicationScoped
public class AuthEndpoint {

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders headers;

    @Inject
    private AuthDAO dao;

    @Inject
    private UserDAO userDao;

    @Inject
    private AuthManager auth;

    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response register(@FormParam("username") String username,
            @FormParam("email") String email, @FormParam("name") String name, @FormParam("lastName") String lastName,
            @FormParam("password") String password) throws URISyntaxException {

        try {
            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(hashPassword(password));
            validate(user);

            userDao.saveUser(user);

            URI uri = UriBuilder.fromUri(uriInfo.getBaseUri().resolve(Navigation.fromAuthRegister(Navigation.OK))).build();
            return Response.seeOther(uri).build();
        } catch (Throwable e) {
            URI uri = UriBuilder.fromUri(uriInfo.getBaseUri().resolve(Navigation.fromAuthRegister(Navigation.ERROR, e))).build();
            return Response.seeOther(uri).build();
        }
    }


    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response login(@FormParam("username") String username,
                          @FormParam("password") String password, @HeaderParam("Auth-Token") String token) throws URISyntaxException {

        String newToken = null;
        try {
            User user = dao.authenticate(username, hashPassword(password));
            if (user != null) {
                newToken = auth.loggedIn(user, token);
            }

            URI redir = uriInfo.getBaseUri().resolve(Navigation.fromLogin(user != null ? Navigation.OK: Navigation.ERROR));
            URI uri = UriBuilder.fromUri(redir).build();
            Response.ResponseBuilder res = Response.seeOther(uri);
            if (newToken != null) {
                res.header("Auth-Token", newToken);
            }
            return res.build();
        } catch (Throwable e) {
            URI uri = UriBuilder.fromUri(uriInfo.getBaseUri().resolve(Navigation.fromLogin(Navigation.ERROR, e))).build();
            return Response.seeOther(uri).build();
        }
    }

    private void validate(User user) {
        // TODO - implement validation
    }

    private String hashPassword(String pass) {
        // FIXME TODO - implement hashing
        return pass;
    }

}
