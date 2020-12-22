package org.openblend.prostalytics.domain;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractPersistentEntity implements Serializable, PersistentEntity {
    private static final long serialVersionUID = 1L;

    @Ignore
    private Key id;

    public AbstractPersistentEntity() {
    }

    public Key getId() {
        return id;
    }

    public void setId(Key id) {
        this.id = id;
    }
}
