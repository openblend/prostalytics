package org.openblend.prostalytics.domain;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Entity;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String KIND = Patient.class.getSimpleName().toUpperCase();

    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String BIRTH_DATE = "birthDate";
    public static final String EXTERNAL_ID = "externalId";

    private long id;

    private String code;
    private String name;
    private String surname;
    private Date birthDate;
    private String externalId;

    public Patient() {
    }

    public Patient(String code, String name, String surname, Date birthDate, String externalId) {
        this.code = code;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.externalId = externalId;
    }

    public static Patient create(Entity entity) {
        Patient p = new Patient();
        p.id = entity.getKey().getId();
        p.code = (String) entity.getProperty(CODE);
        p.name = (String) entity.getProperty(NAME);
        p.surname = (String) entity.getProperty(SURNAME);
        p.birthDate = (Date) entity.getProperty(BIRTH_DATE);
        p.externalId = (String) entity.getProperty(EXTERNAL_ID);
        return p;
    }

    public Entity toEntity() {
        Entity e = new Entity(KIND);
        e.setProperty(CODE, code);
        e.setProperty(NAME, name);
        e.setProperty(SURNAME, surname);
        e.setProperty(BIRTH_DATE, birthDate);
        e.setProperty(EXTERNAL_ID, externalId);
        return e;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
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
}
