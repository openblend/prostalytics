package org.openblend.prostalytics.auth;

import org.openblend.prostalytics.auth.impl.SessionAuthManagerImpl;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public class AuthManagerProducer {

    @Produces
    @SessionScoped
    public AuthManager newAuthManager() {
        HttpServletRequest req = WebApiFilter.getCurrentRequest();
        if (req == null) {
            throw new IllegalStateException("No current ServletRequest!");
        }

        return new SessionAuthManagerImpl(req.getSession());
    }
}
