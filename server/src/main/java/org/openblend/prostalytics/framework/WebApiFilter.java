package org.openblend.prostalytics.framework;

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
@WebFilter(filterName="WebApiFilter")
public class WebApiFilter extends ProtectedContext implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            setCurrentRequest((HttpServletRequest) request);
            setCurrentResponse((HttpServletResponse) response);

            filterChain.doFilter(request, response);

        } finally {
            removeCurrentRequest();
            removeCurrentResponse();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        begin();
    }

    @Override
    public void destroy() {
        end();
    }
}
