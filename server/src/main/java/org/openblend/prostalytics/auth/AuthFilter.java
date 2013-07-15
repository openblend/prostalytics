package org.openblend.prostalytics.auth;

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

    private static List<Pattern> securedResources = new LinkedList<Pattern>();

    private static List<Pattern> openResources = new LinkedList<Pattern>();

    static {
        securedResources.add(Pattern.compile("/rest/auth/.*"));

        openResources.add(Pattern.compile("/rest/auth/register"));
        openResources.add(Pattern.compile("/rest/auth/login"));
        //openResources.add(Pattern.compile("/home.jsf"));
        //openResources.add(Pattern.compile("/register.html"));
        //openResources.add(Pattern.compile("/login.html"));
    }

    @Inject
    private AuthManager authManager;

    private boolean isAuthRequired(HttpServletRequest req) {

        String path = getRequestURI(req);
        for (Pattern p: openResources) {
            if (p.matcher(path).matches())
                return false;
        }
        for (Pattern p: securedResources) {
            if (p.matcher(path).matches())
                return true;
        }
        return false;
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

        if (authManager.getUser() == null && isAuthRequired(req)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
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
