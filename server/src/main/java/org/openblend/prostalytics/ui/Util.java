package org.openblend.prostalytics.ui;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

public class Util {
	
	@Produces
	@Named("ctxRoot")
	public String contextRoot() {
        FacesContext fCtx = FacesContext.getCurrentInstance();
        return fCtx.getExternalContext().getRequestContextPath() + "/";
	}

    @RequestScoped
    public HttpSession getHttpSession() {
        FacesContext fCtx = FacesContext.getCurrentInstance();
        return (HttpSession) fCtx.getExternalContext().getSession(true);
    }

}
