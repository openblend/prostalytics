package org.openblend.prostalytics.domain;

import com.google.appengine.api.datastore.Entity;

import java.io.Serializable;


/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String KIND = Token.class.getSimpleName().toUpperCase();
    public static final String TOKEN = "token";
    public static final String PERSON_ID = "personId";

    private long id;
    private String token;
    private long personId;

    public Token() {
    }

    public Token (String token, long personId) {
        this.token = token;
        this.personId = personId;
    }

    public static Token create(Entity entity) {
        Token t = new Token();
        t.id = entity.getKey().getId();
        t.token = (String) entity.getProperty(TOKEN);
        t.personId = (Long) entity.getProperty(PERSON_ID);
        return t;
    }

    public Entity toEntity() {
        Entity e = new Entity(KIND);
        e.setProperty(TOKEN, token);
        e.setProperty(PERSON_ID, personId);
        return e;
    }

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public long getPersonId() {
        return personId;
    }
}
