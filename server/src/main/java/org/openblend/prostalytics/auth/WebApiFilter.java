package org.openblend.prostalytics.auth;

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

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
@WebFilter("/*")
public class WebApiFilter implements Filter {

    private static ThreadLocal<HttpServletRequest> curReq = new ThreadLocal<HttpServletRequest>();

    private static ThreadLocal<HttpServletResponse> curRes = new ThreadLocal<HttpServletResponse>();

    private static boolean inited;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            curReq.set((HttpServletRequest) request);
            curRes.set((HttpServletResponse) response);

            filterChain.doFilter(request, response);

        } finally {
            curReq.remove();
            curRes.remove();
        }
    }

    static HttpServletRequest getCurrentRequest() {
        if (!inited)
            throw new IllegalStateException("WebApiFilter not installed!");

        return curReq.get();
    }

    static HttpServletResponse getCurrentResponse() {
        if (!inited)
            throw new IllegalStateException("WebApiFilter not installed!");

        return curRes.get();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        inited = true;
    }

    @Override
    public void destroy() {
        inited = false;
    }
}
