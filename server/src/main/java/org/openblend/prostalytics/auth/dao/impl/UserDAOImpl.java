package org.openblend.prostalytics.auth.dao.impl;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import org.openblend.prostalytics.auth.dao.UserDAO;
import org.openblend.prostalytics.dao.impl.AbstractDAOImpl;
import org.openblend.prostalytics.auth.domain.User;

import java.util.concurrent.Callable;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public class UserDAOImpl extends AbstractDAOImpl implements UserDAO {

    protected static Key toKey(long id) {
        return toKey(User.KIND, id);
    }

    @Override
    public long saveUser(final User user) {
        return inTx(new Callable<Long>() {
            public Long call() throws Exception {
                return ds.put(user.toEntity()).getId();
            }
        });
    }

    @Override
    public User loadUser(final long userId) {
        try {
            return User.create(ds.get(toKey(userId)));
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(final long userId) {
        inTx(new Callable<Void>() {
            public Void call() throws Exception {
                ds.delete(toKey(userId));
                return null;
            }
        });
    }
}
