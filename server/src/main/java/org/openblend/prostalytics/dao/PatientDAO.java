package org.openblend.prostalytics.dao;

import java.util.List;

import org.openblend.prostalytics.domain.Patient;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface PatientDAO {
    long savePatient(Patient patient);
    Patient loadPatient(long id);
    void deletePatient(long id);

    List<Patient> findPatients(String code, String name, String surname, String externalId);
}
