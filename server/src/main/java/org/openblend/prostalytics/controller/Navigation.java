package org.openblend.prostalytics.controller;

import org.openblend.prostalytics.framework.ProtectedContext;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public class Navigation extends ProtectedContext {

    public static final String OK = "ok";
    public static final String ERROR = "error";

    public static String fromAuthRegister(String status) {
        return fromAuthRegister(status, null);
    }

    public static String fromAuthRegister(String status, Throwable e) {
        return getContext() + ContextNavigation.fromAuthRegister(status, e);
    }

    public static String fromLogin(String status) {
        return fromLogin(status, null);
    }

    public static String fromLogin(String status, Throwable e) {
        return getContext() + ContextNavigation.fromLogin(status, e);
    }

    private static String getContext() {
        return ProtectedContext.getCurrentRequest().getContextPath();
    }

    public static String toLogin() {
        return getContext() + ContextNavigation.toLogin();
    }
}
