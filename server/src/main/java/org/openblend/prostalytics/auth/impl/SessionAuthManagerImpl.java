package org.openblend.prostalytics.auth.impl;

import org.openblend.prostalytics.framework.ProtectedContext;
import org.openblend.prostalytics.auth.AuthManager;
import org.openblend.prostalytics.auth.domain.User;

import javax.enterprise.inject.Alternative;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
@Alternative
public class SessionAuthManagerImpl extends ProtectedContext implements AuthManager {

    private static String USER = "_logged_in_user";

    @Override
    public User associate(String token) {
        return getUser();
    }

    @Override
    public void dissociate() {
        // no-op
    }

    @Override
    public User getUser() {
        return (User) getSession().getAttribute(USER);
    }

    @Override
    public String loggedIn(User user, String oldToken) {
        // null out the password - just in case ...
        user.setPassword(null);
        getSession().setAttribute(USER, user);
        return null;
    }

    @Override
    public void logout() {
        getSession().invalidate();
    }

    private HttpSession getSession() {
        HttpServletRequest req = ProtectedContext.getCurrentRequest();
        if (req == null) {
            throw new IllegalStateException("No current ServletRequest!");
        }
        return req.getSession();
    }
}
