package org.openblend.prostalytics.domain;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ChildrenField extends ParentField {
    private Class<? extends Collection> collectionType;

    public ChildrenField(Field field, Class<? extends PersistentEntity> type, Class<? extends Collection> collectionType) {
        super(field, type);
        this.collectionType = collectionType;
    }

    protected Class<? extends Collection> getCollectionType() {
        return collectionType;
    }

    public Object saveValue(Object value) {
        return ReflectionUtils.saveChildren(value);
    }

    @Override
    public Object loadValue(Object value) throws Exception {
        return ReflectionUtils.loadChildren(getType(), getCollectionType(), value);
    }
}
