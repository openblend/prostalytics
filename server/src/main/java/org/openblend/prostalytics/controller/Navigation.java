package org.openblend.prostalytics.controller;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public class Navigation {
    public static final String OK = "ok";
    public static final String ERROR = "error";

    public static String fromAuthRegister(String status) {
        return fromAuthRegister(status, null);
    }

    public static String fromAuthRegister(String status, Throwable e) {
        if (OK.equals(status)) {
            return "home.xthml";
        } else if (ERROR.equals(status)) {
            return "register.html";
        }
        throw new IllegalArgumentException("status = " + status);
    }

    public static String fromLogin(String status) {
        return fromLogin(status, null);
    }

    public static String fromLogin(String status, Throwable e) {
        if (OK.equals(status)) {
            return "home.xthml";
        } else if (ERROR.equals(status)) {
            return "login.html";
        }
        throw new IllegalArgumentException("status = " + status);
    }
}
