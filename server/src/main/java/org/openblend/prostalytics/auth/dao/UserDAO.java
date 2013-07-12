package org.openblend.prostalytics.auth.dao;

import org.openblend.prostalytics.auth.domain.User;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public interface UserDAO {

    long saveUser(User user);

    User loadUser(long userId);

    void deleteUser(long userId);
}
