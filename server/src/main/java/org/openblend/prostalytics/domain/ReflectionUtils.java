package org.openblend.prostalytics.domain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ReflectionUtils {
    private static final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    public static Entity toEntity(PersistentEntity target) {
        try {
            Key id = target.getId();
            Entity entity = (id == null) ? new Entity(target.getKind()) : new Entity(id);
            toEntity(target, entity, target.getClass());
            ds.put(entity);
            return entity;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T extends PersistentEntity> T fromEntity(Class<T> targetClass, Entity entity) {
        try {
            T instance = targetClass.newInstance();
            instance.setId(entity.getKey());
            fromEntity(instance, entity, targetClass);
            return instance;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected static void toEntity(PersistentEntity target, Entity entity, Class<?> current) throws Exception {
        if (current == Object.class) {
            return;
        }

        for (Field field : current.getDeclaredFields()) {
            if (field.isAnnotationPresent(Ignore.class)) {
                continue;
            }
            field.setAccessible(true);
            Object value = field.get(target);
            if (value == null) {
                continue;
            }

            if (field.isAnnotationPresent(Parent.class)) {
                value = saveParent(value);
            } else if (field.isAnnotationPresent(Children.class)) {
                value = saveChildren(value);
            }

            entity.setProperty(field.getName(), value);
        }

        toEntity(target, entity, current.getSuperclass());
    }

    protected static Object saveParent(Object parent) {
        if (parent instanceof PersistentEntity == false) {
            throw new IllegalArgumentException("Parent should be instance of PersistentEntity: " + parent);
        }
        PersistentEntity pe = PersistentEntity.class.cast(parent);
        Key key = pe.getId();
        if (key == null) {
            Entity parentEntity = toEntity(pe);
            ds.put(parentEntity);
            key = parentEntity.getKey();
        }
        return key;
    }

    @SuppressWarnings("unchecked")
    private static Object saveChildren(Object value) {
        if (value instanceof Iterable == false) {
            throw new IllegalArgumentException("Children should be instance of Iterable: " + value);
        }
        Iterable<PersistentEntity> iter = Iterable.class.cast(value);
        List<Key> keys = new ArrayList<Key>();
        for (PersistentEntity pe : iter) {
            Key key = pe.getId();
            if (key == null) {
                Entity childEntity = toEntity(pe);
                ds.put(childEntity);
                keys.add(childEntity.getKey());
            }
        }
        return keys;
    }

    @SuppressWarnings("unchecked")
    protected static void fromEntity(PersistentEntity target, Entity entity, Class<?> current) throws Exception {
        if (current == Object.class) {
            return;
        }

        for (Field field : current.getDeclaredFields()) {
            if (field.isAnnotationPresent(Ignore.class)) {
                continue;
            }
            field.setAccessible(true);

            Object value = entity.getProperty(field.getName());
            if (value == null) {
                continue;
            }

            if (field.isAnnotationPresent(Parent.class)) {
                Class<?> clazz = field.getAnnotation(Parent.class).value();
                if (clazz == void.class) {
                    clazz = field.getType();
                }
                if (clazz.isAssignableFrom(PersistentEntity.class) == false) {
                    throw new IllegalArgumentException("Parent class should be instance of PersistentEntity: " + clazz.getName());
                }
                Class<? extends PersistentEntity> targetClass = (Class<? extends PersistentEntity>) clazz;
                value = loadParent(targetClass, value);
            } else if (field.isAnnotationPresent(Children.class)) {
                Children children = field.getAnnotation(Children.class);
                Class<? extends PersistentEntity> targetClass = children.value();
                Class<? extends Collection> collectionClass = children.collection();
                value = loadChildren(targetClass, collectionClass, value);
            }

            field.set(target, value);
        }

        fromEntity(target, entity, current.getSuperclass());
    }

    protected static Object loadParent(Class<? extends PersistentEntity> targetClass, Object value) throws Exception {
        if (value instanceof Key == false) {
            throw new IllegalArgumentException("Parent should be saved as Key: " + value);
        }
        Key id = Key.class.cast(value);
        Entity entity = ds.get(id);
        return fromEntity(targetClass, entity);
    }

    @SuppressWarnings("unchecked")
    protected static Object loadChildren(Class<? extends PersistentEntity> targetClass, Class<? extends Collection> collectionClass, Object value) throws Exception {
        if (value instanceof Iterable == false) {
            throw new IllegalArgumentException("Parent should be saved as Iterable<Key>: " + value);
        }
        Collection<Object> collection = collectionClass.newInstance();
        Iterable<Key> ids = Iterable.class.cast(value);
        for (Key id : ids) {
            Entity entity = ds.get(id);
            collection.add(fromEntity(targetClass, entity));
        }
        return collection;
    }
}
