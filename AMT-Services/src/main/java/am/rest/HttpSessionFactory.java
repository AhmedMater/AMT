package am.rest;

import org.glassfish.hk2.api.Factory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by mohamed.elewa on 16/03/2017.
 */
public class HttpSessionFactory implements Factory<HttpSession> {

    private final HttpServletRequest request;

    @Inject
    public HttpSessionFactory(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public HttpSession provide() {
        return request.getSession();
    }

    @Override
    public void dispose(HttpSession t) {
    }
}