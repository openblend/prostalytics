package org.openblend.prostalytics.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.openblend.prostalytics.dao.PatientDAO;
import org.openblend.prostalytics.domain.Patient;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class PatientDAOImpl extends AbstractDAOImpl implements PatientDAO {
    protected static Key toKey(long id) {
        return toKey(Patient.KIND, id);
    }

    protected static final Function<Entity, Patient> FN = new Function<Entity, Patient>() {
        public Patient apply(Entity input) {
            return Patient.create(input);
        }
    };

    public long savePatient(final Patient patient) {
        return inTx(new Callable<Long>() {
            public Long call() throws Exception {
                return ds.put(patient.toEntity()).getId();
            }
        });
    }

    public Patient loadPatient(long id) {
        try {
            return Patient.create(ds.get(toKey(id)));
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePatient(final long id) {
        inTx(new Callable<Void>() {
            public Void call() throws Exception {
                ds.delete(toKey(id));
                return null;
            }
        });
    }

    public List<Patient> findPatients(String code, String name, String surname, String externalId) {
        Query query = new Query(Patient.KIND);

        List<Query.Filter> filters = new ArrayList<Query.Filter>();
        if (code != null) filters.add(new Query.FilterPredicate(Patient.CODE, Query.FilterOperator.EQUAL, code));
        if (name != null) filters.add(new Query.FilterPredicate(Patient.NAME, Query.FilterOperator.EQUAL, name));
        if (surname != null) filters.add(new Query.FilterPredicate(Patient.SURNAME, Query.FilterOperator.EQUAL, surname));
        if (externalId != null) filters.add(new Query.FilterPredicate(Patient.EXTERNAL_ID, Query.FilterOperator.EQUAL, externalId));

        if (filters.size() > 0) {
            query.setFilter(new Query.CompositeFilter(Query.CompositeFilterOperator.AND, filters));
        }

        PreparedQuery pq = ds.prepare(query);
        return Lists.transform(pq.asList(FetchOptions.Builder.withDefaults()), FN);
    }
}
