package org.openblend.prostalytics.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:marko.strukelj@gmail.com">Marko Strukelj</a>
 */
public class ProtectedContext {

    private static ThreadLocal<HttpServletRequest> curReq = new ThreadLocal<HttpServletRequest>();

    private static ThreadLocal<HttpServletResponse> curRes = new ThreadLocal<HttpServletResponse>();

    private static boolean inited;

    protected static HttpServletRequest getCurrentRequest() {
        if (!inited)
            throw new IllegalStateException("WebApiFilter not installed!");

        return curReq.get();
    }

    protected static HttpServletResponse getCurrentResponse() {
        if (!inited)
            throw new IllegalStateException("WebApiFilter not installed!");

        return curRes.get();
    }

    protected static void setCurrentRequest(HttpServletRequest req) {
        curReq.set(req);
    }

    protected static void setCurrentResponse(HttpServletResponse res) {
        curRes.set(res);
    }

    protected static void begin() {
        inited = true;
    }

    protected static void end() {
        inited = false;
    }

    protected static void removeCurrentRequest() {
        curReq.remove();
    }

    protected static void removeCurrentResponse() {
        curRes.remove();
    }
}
