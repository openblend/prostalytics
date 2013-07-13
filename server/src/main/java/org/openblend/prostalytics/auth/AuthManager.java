package org.openblend.prostalytics.auth;

import org.openblend.prostalytics.auth.domain.User;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public interface AuthManager {

    User associate(String token);

    void dissociate();

    User getUser();

    String loggedIn(User user, String oldToken);
}
