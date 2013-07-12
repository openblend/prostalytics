package org.openblend.prostalytics.auth.domain;

import com.google.appengine.api.datastore.Entity;
import com.google.common.base.Function;

import java.io.Serializable;


/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Function<Entity, Token> FN = new Function<Entity, Token>() {
        public Token apply(Entity input) {
            return Token.create(input);
        }
    };

    public static final String KIND = Token.class.getSimpleName().toUpperCase();
    public static final String TOKEN = "token";
    public static final String USER_ID = "userId";

    private long id;
    private String token;
    private long userId;

    public Token() {
    }

    public Token (String token, long userId) {
        this.token = token;
        this.userId = userId;
    }

    public static Token create(Entity entity) {
        Token t = new Token();
        t.id = entity.getKey().getId();
        t.token = (String) entity.getProperty(TOKEN);
        t.userId = (Long) entity.getProperty(USER_ID);
        return t;
    }

    public Entity toEntity() {
        Entity e = new Entity(KIND);
        e.setProperty(TOKEN, token);
        e.setProperty(USER_ID, userId);
        return e;
    }

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public long getUserId() {
        return userId;
    }
}
