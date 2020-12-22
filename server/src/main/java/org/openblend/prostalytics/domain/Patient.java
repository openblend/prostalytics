package org.openblend.prostalytics.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Patient extends AbstractPersistentEntity {
    private static final long serialVersionUID = 1L;

    public static final String KIND = Patient.class.getSimpleName().toUpperCase();

    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String BIRTH_DATE = "birthDate";
    public static final String EXTERNAL_ID = "externalId";

    private String code;
    private String name;
    private String surname;
    private Date birthDate;
    private String externalId;
    @Children(Followup.class) private List<Followup> followups;

    public Patient() {
    }

    public Patient(String code, String name, String surname, Date birthDate, String externalId) {
        this.code = code;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.externalId = externalId;
    }

    public String getKind() {
        return KIND;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getExternalId() {
        return externalId;
    }

    public List<Followup> getFollowups() {
        return Collections.unmodifiableList(followups);
    }

    public synchronized void addFollowup(Followup followup) {
        if (followups == null) {
            followups = new ArrayList<Followup>();
        }
        followup.setPatient(this);
        followups.add(followup);
    }

    public synchronized void removeFollowup(Followup followup) {
        if (followups != null) {
            followup.setPatient(null);
            followups.remove(followup);
        }
    }
}
