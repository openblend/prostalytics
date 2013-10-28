package org.openblend.prostalytics.domain;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DefaultField implements PersistentField {
    private Field field;

    public DefaultField(Field field) {
        this.field = field;
    }

    public String getName() {
        return field.getName();
    }

    public Object get(Object target) throws Exception {
        return field.get(target);
    }

    public void set(Object target, Object value) throws Exception {
        field.set(target, value);
    }

    public Object saveValue(Object value) {
        return value;
    }

    public Object loadValue(Object value) throws Exception {
        return value;
    }
}
