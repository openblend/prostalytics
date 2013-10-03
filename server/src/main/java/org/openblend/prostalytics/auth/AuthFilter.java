package org.openblend.prostalytics.auth;

import org.openblend.prostalytics.auth.dao.AuthDAO;
import org.openblend.prostalytics.controller.Navigation;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
@WebFilter(filterName="AuthFilter")
public class AuthFilter implements Filter {

    private static final boolean AUTH_REQUIRED_BY_DEFAULT = false;

    private static final boolean LOGIN_REDIRECTED_BY_DEFAULT = true;

    /** Resources only available to authenticated users */
    private static List<Pattern> securedResources = new LinkedList<Pattern>();

    /** Resources openly available at application install time until application is considered installed. */
    private static List<Pattern> setupResources = new LinkedList<Pattern>();

    /** Resources available only to admins */
    private static List<Pattern> adminResources = new LinkedList<Pattern>();

    /** Public resources that don't require authentication */
    private static List<Pattern> openResources = new LinkedList<Pattern>();

    /** Secured resources that trigger login redirect if user is not authenticated i.e. UI resources*/
    private static List<Pattern> loginRedirectedResources = new LinkedList<Pattern>();

    /** Secured resources that don't trigger login redirect i.e. REST endpoints */
    private static List<Pattern> notLoginRedirectedResources = new LinkedList<Pattern>();

    static {
        securedResources.add(Pattern.compile("/rest/auth/.*"));
        securedResources.add(Pattern.compile("/form.jsf"));
        securedResources.add(Pattern.compile("/home.jsf"));

        adminResources.add(Pattern.compile("/rest/auth/register"));
        setupResources.add(Pattern.compile("/rest/auth/register"));
        openResources.add(Pattern.compile("/rest/auth/login"));
/*
        openResources.add(Pattern.compile("/home.jsf"));
        openResources.add(Pattern.compile("/register.html"));
        openResources.add(Pattern.compile("/login.html"));
        openResources.add(Pattern.compile("/css/.*"));
        openResources.add(Pattern.compile("/js/.*"));
        openResources.add(Pattern.compile("/lib/.*"));
        openResources.add(Pattern.compile("/images/.*"));
        openResources.add(Pattern.compile("/partials/.*"));
*/
        notLoginRedirectedResources.add(Pattern.compile("/rest/.*"));
    }

    @Inject
    private AuthManager authManager;

    @Inject
    private AuthDAO dao;

    private boolean isAuthRequired(HttpServletRequest req) {

        String path = getRequestURI(req);
        for (Pattern p: openResources) {
            if (p.matcher(path).matches())
                return false;
        }

        for (Pattern p: securedResources) {
            if (p.matcher(path).matches()) {
                // setup resources are open if app has not been set up yet
                for (Pattern r: setupResources) {
                    if (r.matcher(path).matches()) {
                        if (dao.findAdmin() == null) {
                            // if admin doesn't exist, it means the app has not been set up yet
                            return false;
                        }
                    }
                }

                return true;
            }
        }
        return AUTH_REQUIRED_BY_DEFAULT;
    }

    private boolean isAuthorized(HttpServletRequest req) {
        // user has to be logged in
        if (authManager.getUser() == null)
            return false;

        // if resource is an admin resource then user has to be admin, to have access
        String path = getRequestURI(req);
        for (Pattern p: adminResources) {
            if (p.matcher(path).matches())
                return authManager.getUser().isAdmin();
        }

        return true;
    }

    private boolean isLoginRedirected(HttpServletRequest req) {

        String path = getRequestURI(req);
        for (Pattern p: notLoginRedirectedResources) {
            if (p.matcher(path).matches())
                return false;
        }
        for (Pattern p: loginRedirectedResources) {
            if (p.matcher(path).matches())
                return true;
        }
        return LOGIN_REDIRECTED_BY_DEFAULT;
    }

    private String getRequestURI(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String token = req.getHeader("Auth-Token");

        if (token != null) {
            authManager.associate(token);
        }

        if (authManager.getUser() == null) {
            if (isAuthRequired(req)) {
                if (isLoginRedirected(req)) {
                    res.sendRedirect(Navigation.toLogin());
                } else {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
                return;
            }
        } else {
            if (!isAuthorized(req)) {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }

        try {
            filterChain.doFilter(req, res);
        } finally {
            authManager.dissociate();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
