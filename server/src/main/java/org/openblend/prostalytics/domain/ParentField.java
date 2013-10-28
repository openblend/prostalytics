package org.openblend.prostalytics.domain;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ParentField extends DefaultField {
    private Class<? extends PersistentEntity> type;

    public ParentField(Field field, Class<? extends PersistentEntity> type) {
        super(field);
        this.type = type;
    }

    protected Class<? extends PersistentEntity> getType() {
        return type;
    }

    @Override
    public Object saveValue(Object value) {
        return ReflectionUtils.saveParent(value);
    }

    public Object loadValue(Object value) throws Exception {
        return ReflectionUtils.loadParent(type, value);
    }
}
