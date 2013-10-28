package org.openblend.prostalytics.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ReflectionUtils {
    private static final ThreadLocal<Map<Object, Object>> reentered = new ThreadLocal<Map<Object, Object>>();
    private static final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    public static Key save(PersistentEntity target) {
        Entity entity = toEntity(target);
        return ds.put(entity);
    }

    public static <T extends PersistentEntity> T load(Class<T> targetClass, Key id) {
        Map<Key, Entity> map = ds.get(Collections.singleton(id));
        if (map.isEmpty() == false) {
            Entity entity = map.entrySet().iterator().next().getValue();
            return fromEntity(targetClass, entity);
        } else {
            return null;
        }
    }

    public static Entity toEntity(PersistentEntity target) {
        Map<Object, Object> current = reentered.get();
        if (current == null) {
            current = new HashMap<Object, Object>();
            reentered.set(current);
        }
        Entity re = (Entity) current.get(target);
        if (re != null) {
            return re;
        }
        try {
            Key id = target.getId();
            Entity entity = (id == null) ? new Entity(target.getKind()) : new Entity(id);
            current.put(target, entity);
            toEntity(target, entity, target.getClass());
            ds.put(entity);
            return entity;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            reentered.remove();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends PersistentEntity> T fromEntity(Class<T> targetClass, Entity entity) {
        Map<Object, Object> current = reentered.get();
        if (current == null) {
            current = new HashMap<Object, Object>();
            reentered.set(current);
        }
        T re = (T) current.get(entity.getKey());
        if (re != null) {
            return re;
        }
        try {
            T instance = targetClass.newInstance();
            instance.setId(entity.getKey());
            current.put(entity.getKey(), instance);
            fromEntity(instance, entity, targetClass);
            return instance;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            reentered.remove();
        }
    }

    protected static void toEntity(PersistentEntity target, Entity entity, Class<? extends PersistentEntity> current) throws Exception {
        MetaModel mm = MetaModel.createMetaModel(current);

        for (PersistentField field : mm.getFields()) {
            Object value = field.get(target);
            if (value == null) {
                continue;
            }
            value = field.saveValue(value);
            entity.setProperty(field.getName(), value);
        }
    }

    static Object saveParent(Object parent) {
        if (parent instanceof PersistentEntity == false) {
            throw new IllegalArgumentException("Parent should be instance of PersistentEntity: " + parent);
        }
        PersistentEntity pe = PersistentEntity.class.cast(parent);
        return toEntity(pe).getKey();
    }

    @SuppressWarnings("unchecked")
    static Object saveChildren(Object value) {
        if (value instanceof Iterable == false) {
            throw new IllegalArgumentException("Children should be instance of Iterable<PersistentEntity>: " + value);
        }
        Iterable<PersistentEntity> iter = Iterable.class.cast(value);
        List<Key> keys = new ArrayList<Key>();
        for (PersistentEntity pe : iter) {
            keys.add(toEntity(pe).getKey());
        }
        return keys;
    }

    @SuppressWarnings("unchecked")
    protected static void fromEntity(PersistentEntity target, Entity entity, Class<? extends PersistentEntity> current) throws Exception {
        MetaModel mm = MetaModel.createMetaModel(current);

        for (PersistentField field : mm.getFields()) {
            Object value = entity.getProperty(field.getName());
            if (value == null) {
                continue;
            }
            value = field.loadValue(value);
            field.set(target, value);
        }
    }

    static Object loadParent(Class<? extends PersistentEntity> targetClass, Object value) throws Exception {
        if (value instanceof Key == false) {
            throw new IllegalArgumentException("Parent should be saved as Key: " + value);
        }
        Key id = Key.class.cast(value);
        return load(targetClass, id);
    }

    @SuppressWarnings("unchecked")
    static Object loadChildren(Class<? extends PersistentEntity> targetClass, Class<? extends Collection> collectionClass, Object value) throws Exception {
        if (value instanceof Iterable == false) {
            throw new IllegalArgumentException("Parent should be saved as Iterable<Key>: " + value);
        }
        Collection<Object> collection = collectionClass.newInstance();
        Iterable<Key> ids = Iterable.class.cast(value);
        for (Key id : ids) {
            collection.add(load(targetClass, id));
        }
        return collection;
    }
}
