package org.openblend.prostalytics.auth.dao;

import org.openblend.prostalytics.auth.domain.Token;
import org.openblend.prostalytics.auth.domain.User;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public interface AuthDAO {

    User findAdmin();

    User findUserByToken(String token);

    Token updateOrCreateToken(User user, String oldToken);

    void invalidateToken(String token);

    User authenticate(String username, String passwordHash);
}
