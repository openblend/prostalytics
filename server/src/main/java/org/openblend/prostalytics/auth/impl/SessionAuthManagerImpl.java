package org.openblend.prostalytics.auth.impl;

import org.apache.deltaspike.core.api.exclude.Exclude;
import org.openblend.prostalytics.auth.AuthManager;
import org.openblend.prostalytics.auth.domain.User;

import javax.servlet.http.HttpSession;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
@Exclude
public class SessionAuthManagerImpl implements AuthManager {

    private static String USER = "_logged_in_user";

    private HttpSession session;

    public SessionAuthManagerImpl(HttpSession session) {
        this.session = session;
    }

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
        return (User) session.getAttribute(USER);
    }

    @Override
    public String loggedIn(User user, String oldToken) {
        // null out the password - just in case ...
        user.setPassword(null);
        session.setAttribute(USER, user);
        return null;
    }
}
