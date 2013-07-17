package org.openblend.prostalytics.auth;

import org.openblend.prostalytics.auth.dao.AuthDAO;
import org.openblend.prostalytics.auth.dao.UserDAO;
import org.openblend.prostalytics.auth.domain.User;
import org.openblend.prostalytics.controller.Navigation;

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

    /*
    * curl --data "username=test2&name=Test&lastname=Tester&email=yourmail@gmail.com&password=test" http://localhost:8080/prostalytics-server-1.0.0-SNAPSHOT/rest/auth/register
    *
    */
    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response register(@FormParam("username") String username,
            @FormParam("email") String email, @FormParam("name") String name, @FormParam("lastname") String lastName,
            @FormParam("password") String password) throws URISyntaxException {

        try {
            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setLastname(lastName);
            user.setEmail(email);
            user.setPassword(hashPassword(password));
            validate(user);

            userDao.saveUser(user);

            URI uri = UriBuilder.fromUri(uriInfo.getBaseUri().resolve(Navigation.fromAuthRegister(Navigation.OK))).build();
            return Response.seeOther(uri).build();
        } catch (Throwable e) {
            e.printStackTrace();  // TODO :)
            URI uri = UriBuilder.fromUri(uriInfo.getBaseUri().resolve(Navigation.fromAuthRegister(Navigation.ERROR, e))).build();
            return Response.seeOther(uri).build();
        }
    }


    /*
     * curl --data '{"username":"test2","name":"Test","lastname":"Tester","email":"yourmail@gmail.com","password":"test"}' http://localhost:8080/prostalytics-server-1.0.0-SNAPSHOT/rest/auth/register --header "Content-Type:application/json"
     *
     */
    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(User user) throws URISyntaxException {

        try {
            user.setPassword(hashPassword(user.getPassword()));
            validate(user);

            long id = userDao.saveUser(user);

            user.setId(id);
            user.setPassword(null); // we don't send around password
            return Response.status(Response.Status.OK).entity(user).build();
        } catch (Throwable e) {
            e.printStackTrace();  // TODO :)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
        }
    }

    /*
     * curl --data "username=test2&password=test" http://localhost:8080/prostalytics-server-1.0.0-SNAPSHOT/rest/auth/login
     *
     */
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

    /*
     * curl --data '{"username":"test2","password":"test"}' http://localhost:8080/prostalytics-server-1.0.0-SNAPSHOT/rest/auth/login --header "Content-Type:application/json"
     *
     */
    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user, @HeaderParam("Auth-Token") String token) throws URISyntaxException {

        String newToken = null;
        try {
            user = dao.authenticate(user.getUsername(), hashPassword(user.getPassword()));
            if (user != null) {
                newToken = auth.loggedIn(user, token);

            }

            Response.ResponseBuilder res = Response.status(Response.Status.OK).entity(user);
            if (newToken != null) {
                res.header("Auth-Token", newToken);
            }
            return res.build();

        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
        }
    }

    private void validate(User user) {
        // TODO - implement validation
    }

    private String hashPassword(String pass) {
        if (pass == null)
            return null;

        // FIXME TODO - implement hashing
        return pass;
    }

}
