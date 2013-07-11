package org.openblend.prostalytics.auth;

import org.openblend.prostalytics.dao.AuthDAO;
import org.openblend.prostalytics.domain.Patient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
@ApplicationScoped
public class TokenManager {

    private static final ThreadLocal<Patient> tlu = new ThreadLocal<Patient>();

    @Inject
    private AuthDAO dao;

    public Patient associate(String token) {
        Patient p = dao.findUserByToken(token);
        if (p != null) {
            tlu.set(p);
        }
        return p;
    }

    public void dissociate() {
        tlu.remove();
    }

    public static Patient getPatient() {
        return tlu.get();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
