package org.openblend.prostalytics.auth.domain;

import java.io.Serializable;

import com.google.appengine.api.datastore.Entity;
import com.google.common.base.Function;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Function<Entity, User> FN = new Function<Entity, User>() {
        public User apply(Entity input) {
            return User.create(input);
        }
    };

    public static final String KIND = User.class.getSimpleName().toUpperCase();
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String LAST_NAME = "lastName";
    public static final String PASSWORD = "password";
    public static final String IS_ADMIN = "isAdmin";


    private long id = -1;
    private String username;
    private String email;
    private String name;
    private String lastName;
    private boolean isAdmin;

    // password should always be hashed
    private String password;


    public User() {

    }

    public static User create(Entity entity) {
        User u = new User();
        u.id = entity.getKey().getId();
        u.username = (String) entity.getProperty(USERNAME);
        u.email = (String) entity.getProperty(EMAIL);
        u.name = (String) entity.getProperty(NAME);
        u.lastName = (String) entity.getProperty(LAST_NAME);
        u.password = (String) entity.getProperty(PASSWORD);
        Boolean val = (Boolean) entity.getProperty(IS_ADMIN);
        u.isAdmin = val != null && val;
        return u;
    }

    public Entity toEntity() {
        Entity e = new Entity(KIND);
        e.setProperty(USERNAME, username);
        e.setProperty(EMAIL, email);
        e.setProperty(NAME, name);
        e.setProperty(LAST_NAME, lastName);
        e.setProperty(PASSWORD, password);
        e.setProperty(IS_ADMIN, isAdmin);
        return e;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastname(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
