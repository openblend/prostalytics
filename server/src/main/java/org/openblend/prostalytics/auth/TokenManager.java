package org.openblend.prostalytics.auth;

import org.openblend.prostalytics.auth.dao.AuthDAO;
import org.openblend.prostalytics.auth.domain.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
@ApplicationScoped
public class TokenManager {

    private static final ThreadLocal<User> tlu = new ThreadLocal<User>();

    @Inject
    private AuthDAO dao;

    public User associate(String token) {
        User u = dao.findUserByToken(token);
        if (u != null) {
            tlu.set(u);
        }
        return u;
    }

    public void dissociate() {
        tlu.remove();
    }

    public static User getUser() {
        return tlu.get();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
