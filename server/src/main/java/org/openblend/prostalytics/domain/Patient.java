package org.openblend.prostalytics.domain;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Patient implements Serializable, PersistentEntity {
    private static final long serialVersionUID = 1L;

    public static final String KIND = Patient.class.getSimpleName().toUpperCase();

    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String BIRTH_DATE = "birthDate";
    public static final String EXTERNAL_ID = "externalId";

    @Ignore
    private Key id;

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

    public Key getId() {
        return id;
    }

    public void setId(Key id) {
        this.id = id;
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
}
