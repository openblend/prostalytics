package org.openblend.prostalytics.domain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class MetaModel {
    private static Map<Class<? extends PersistentEntity>, MetaModel> cache = new HashMap<Class<? extends PersistentEntity>, MetaModel>();

    private List<PersistentField> fields = new ArrayList<PersistentField>();

    private MetaModel() {
    }

    public synchronized static MetaModel createMetaModel(Class<? extends PersistentEntity> clazz) throws Exception {
        MetaModel mm = cache.get(clazz);
        if (mm == null) {
            mm = new MetaModel();
            mm.checkFields(clazz);
            cache.put(clazz, mm);
        }
        return mm;
    }

    @SuppressWarnings("unchecked")
    protected void checkFields(Class<?> current) throws Exception {
        if (current == Object.class) {
            return;
        }

        for (Field field : current.getDeclaredFields()) {
            if (field.isAnnotationPresent(Ignore.class)) {
                continue;
            }
            field.setAccessible(true);

            if (field.isAnnotationPresent(Parent.class)) {
                Class<?> clazz = field.getAnnotation(Parent.class).value();
                if (clazz == void.class) {
                    clazz = field.getType();
                }
                if (clazz.isAssignableFrom(PersistentEntity.class) == false) {
                    throw new IllegalArgumentException("Parent class should be instance of PersistentEntity: " + clazz.getName());
                }
                Class<? extends PersistentEntity> targetClass = (Class<? extends PersistentEntity>) clazz;
                fields.add(new ParentField(field, targetClass));
            } else if (field.isAnnotationPresent(Children.class)) {
                Children children = field.getAnnotation(Children.class);
                Class<? extends PersistentEntity> targetClass = children.value();
                Class<? extends Collection> collectionClass = children.collection();
                fields.add(new ChildrenField(field, targetClass, collectionClass));
            } else {
                fields.add(new DefaultField(field));
            }
        }

        checkFields(current.getSuperclass());
    }

    public List<PersistentField> getFields() {
        return fields;
    }
}
