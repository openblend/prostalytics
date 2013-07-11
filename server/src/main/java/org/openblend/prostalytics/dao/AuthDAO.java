package org.openblend.prostalytics.dao;

import org.openblend.prostalytics.domain.Patient;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public interface AuthDAO {

    public Patient findUserByToken(String token);
}
