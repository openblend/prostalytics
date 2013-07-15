package org.openblend.prostalytics.auth.impl;

import org.openblend.prostalytics.auth.AuthManager;
import org.openblend.prostalytics.auth.dao.AuthDAO;
import org.openblend.prostalytics.auth.domain.Token;
import org.openblend.prostalytics.auth.domain.User;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.util.UUID;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
@Alternative
public class TokenAuthManagerImpl implements AuthManager {

    private static final ThreadLocal<User> tlu = new ThreadLocal<User>();

    @Inject
    private AuthDAO dao;

    @Override
    public User associate(String token) {
        User u = dao.findUserByToken(token);
        if (u != null) {
            tlu.set(u);
        }
        return u;
    }

    @Override
    public void dissociate() {
        tlu.remove();
    }

    @Override
    public User getUser() {
        return tlu.get();
    }

    @Override
    public String loggedIn(User user, String oldToken) {
        Token token = dao.updateOrCreateToken(user, oldToken);
        // null out the password - just in case ...
        user.setPassword(null);
        tlu.set(user);
        return token == null ? null : token.getToken();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
