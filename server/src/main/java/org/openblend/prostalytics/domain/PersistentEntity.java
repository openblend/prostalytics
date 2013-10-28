package org.openblend.prostalytics.domain;

import com.google.appengine.api.datastore.Key;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface PersistentEntity {
    Key getId();
    void setId(Key id);

    String getKind();
}
