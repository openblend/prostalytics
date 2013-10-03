package org.openblend.prostalytics.dao;

import org.openblend.prostalytics.auth.dao.AuthDAO;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
@Path("/appinfo")
public class AppInfoEndpoint {

    @Inject
    private AuthDAO dao;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public AppInfo getAppInfo() {
        AppInfo ret = new AppInfo();
        ret.setSetUp(dao.findAdmin() != null);
        return ret;
    }
}
