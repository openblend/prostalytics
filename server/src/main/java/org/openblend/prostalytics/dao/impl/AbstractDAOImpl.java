package org.openblend.prostalytics.dao.impl;

import java.util.concurrent.Callable;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractDAOImpl {
    protected final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    protected static Key toKey(String kind, long id) {
        return KeyFactory.createKey(kind, id);
    }

    protected <T> T inTx(Callable<T> callable) {
        Transaction tx = ds.beginTransaction();
        boolean ok = false;
        try {
            T result = callable.call();
            ok = true;
            return result;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (ok)
                tx.commit();
            else
                tx.rollback();
        }
    }
}
