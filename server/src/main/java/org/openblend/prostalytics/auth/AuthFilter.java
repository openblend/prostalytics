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
import java.util.HashSet;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    private static HashSet<String> openResources = new HashSet<String>();

    static {
        openResources.add("/rest/auth/register");
        openResources.add("/rest/auth/login");
        openResources.add("/home.xhtml");
        openResources.add("/register.html");
        openResources.add("/login.html");
    }

    @Inject
    private AuthManager authManager;

    private boolean isAuthRequired(HttpServletRequest req) {
        if (openResources.contains(req.getPathInfo())) {
            return false;
        }

        return true;
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
