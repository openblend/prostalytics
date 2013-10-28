package org.openblend.prostalytics.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import com.google.appengine.api.datastore.KeyFactory;
import org.openblend.prostalytics.dao.PatientDAO;
import org.openblend.prostalytics.domain.Patient;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.openblend.prostalytics.domain.ReflectionUtils;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class PatientDAOImpl extends AbstractDAOImpl implements PatientDAO {
    protected static final Function<Entity, Patient> FN = new Function<Entity, Patient>() {
        @Override
        public Patient apply(Entity input) {
            return ReflectionUtils.fromEntity(Patient.class, input);
        }
    };

    @Override
    public String savePatient(final Patient patient) {
        return inTx(new Callable<String>() {
            @Override
            public String call() throws Exception {
                //TODO update existing patients
                //generate code for new inserts
                String code = patient.getCode();
                if (code == null || "".equals(code)){
                    patient.setCode(randomString(5));
                }
                Entity entity = ReflectionUtils.toEntity(patient);
                Key key = ds.put(entity);
                return KeyFactory.keyToString(key);
            }
        });
    }

    @Override
    public Patient loadPatient(String id) {
        try {
            return ReflectionUtils.fromEntity(Patient.class, ds.get(KeyFactory.stringToKey(id)));
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePatient(final String id) {
        inTx(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ds.delete(KeyFactory.stringToKey(id));
                return null;
            }
        });
    }

    @Override
    public List<Patient> findPatients(String code, String name, String surname, String externalId) {
        Query query = new Query(Patient.KIND);

        List<Query.Filter> filters = new ArrayList<Query.Filter>();
        if (notEmpty(code)) filters.add(new Query.FilterPredicate(Patient.CODE, Query.FilterOperator.EQUAL, code));
        if (notEmpty(name)) filters.add(new Query.FilterPredicate(Patient.NAME, Query.FilterOperator.EQUAL, name));
        if (notEmpty(surname)) filters.add(new Query.FilterPredicate(Patient.SURNAME, Query.FilterOperator.EQUAL, surname));
        if (notEmpty(externalId)) filters.add(new Query.FilterPredicate(Patient.EXTERNAL_ID, Query.FilterOperator.EQUAL, externalId));

        if (filters.size() > 1) {
            query.setFilter(new Query.CompositeFilter(Query.CompositeFilterOperator.AND, filters));
        } else if (filters.size() > 0) {
            query.setFilter(filters.get(0));
        }

        PreparedQuery pq = ds.prepare(query);
        return Lists.transform(pq.asList(FetchOptions.Builder.withDefaults()), FN);
    }

    private boolean notEmpty(String field) {
        return field != null && !field.equals("");
    }

    private String randomString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

}
