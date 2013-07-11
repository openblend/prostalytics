package org.openblend.prostalytics.dao.impl;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.openblend.prostalytics.dao.AuthDAO;
import org.openblend.prostalytics.dao.PatientDAO;
import org.openblend.prostalytics.domain.Patient;
import org.openblend.prostalytics.domain.Token;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public class AuthDAOImpl extends AbstractDAOImpl implements AuthDAO {

    @Inject
    private PatientDAO patientDao;

    protected static final Function<Entity, Token> FN = new Function<Entity, Token>() {
        public Token apply(Entity input) {
            return Token.create(input);
        }
    };

    @Override
    public Patient findUserByToken(String token) {
        Query query = new Query(Patient.KIND);

        List<Query.Filter> filters = new ArrayList<Query.Filter>();
        filters.add(new Query.FilterPredicate(Token.TOKEN, Query.FilterOperator.EQUAL, token));

        PreparedQuery pq = ds.prepare(query);
        List<Token> tokens = Lists.transform(pq.asList(FetchOptions.Builder.withDefaults()), FN);

        if (tokens.size() == 0)
            return null;

        long id = tokens.get(0).getPersonId();
        return patientDao.loadPatient(id);
    }
}
