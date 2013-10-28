package org.openblend.prostalytics.domain;

import java.util.Date;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Followup extends AbstractPersistentEntity {
    private static final long serialVersionUID = 1L;
    public static final String KIND = Followup.class.getSimpleName().toUpperCase();

    @Parent private Patient patient;
    private Date date;

    public Followup() {
    }

    public String getKind() {
        return KIND;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
