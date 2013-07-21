package org.openblend.prostalytics.controller;

import static org.openblend.prostalytics.controller.Navigation.OK;
import static org.openblend.prostalytics.controller.Navigation.ERROR;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public class ContextNavigation {

    public static String fromAuthRegister(String status) {
        return fromAuthRegister(status, null);
    }

    public static String fromAuthRegister(String status, Throwable e) {
        if (OK.equals(status)) {
            return "/login.html";
        } else if (ERROR.equals(status)) {
            return "/register.html";
        }
        throw new IllegalArgumentException("status = " + status);
    }

    public static String fromLogin(String status) {
        return fromLogin(status, null);
    }

    public static String fromLogin(String status, Throwable e) {
        if (OK.equals(status)) {
            return "/home.jsf";
        } else if (ERROR.equals(status)) {
            return "/login.html";
        }
        throw new IllegalArgumentException("status = " + status);
    }

    public static String toLogin() {
        return "/login.html";
    }
}
