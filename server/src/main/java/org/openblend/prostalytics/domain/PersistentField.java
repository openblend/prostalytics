package org.openblend.prostalytics.domain;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface PersistentField {
    String getName();
    Object get(Object target) throws Exception;
    void set(Object target, Object value) throws Exception;
    Object saveValue(Object value);
    Object loadValue(Object value) throws Exception;
}
