/*
 * Copyright 2009 Rodney Schneider
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * under the License.
 */
package com.vaadin.terminal.gwt.server;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import com.vaadin.Application;

/**
 * @author Les Hazlewood
 * @author Rodney Schneider
 */
@SuppressWarnings("serial")
public class GrailsAwareGAEApplicationServlet extends GAEApplicationServlet {

    private static final transient Logger log = LoggerFactory.getLogger(GrailsAwareGAEApplicationServlet.class);

    /**
     * Name of the class that extends com.vaadin.Application. We don't retain
     * the actual Class instance directly due to Grails class reloading - the
     * Application class or any that it references could be reloaded dynamically
     * at runtime during development, so we look up the class with this name
     * from the classloader every time it is requested when creating a new
     * Application instance.
     */
    private String applicationClassName;

    private ClassLoader doGetClassLoader() {
        return ApplicationHolder.getApplication().getClassLoader();
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    protected Class<? extends Application> getApplicationClass() throws UnavailableClassException {
        try {
            return (Class<? extends Application>) this.doGetClassLoader().loadClass(this.applicationClassName);
        } catch (final ClassNotFoundException e) {
            throw new UnavailableClassException(e);
        }
    }

    /**
     * Returns the Grails ClassLoader, because the servlet's ClassLoader can't
     * see the Grails application source.
     * 
     * @return the ClassLoader stored in the {@link GrailsApplication}
     * @throws ServletException
     */
    @Override
    protected ClassLoader getClassLoader() throws ServletException {
        return this.doGetClassLoader();
    }

    @Override
    protected Application getNewApplication(final HttpServletRequest request) throws ServletException {
        Application app = null;

        try {
            app = (Application) ApplicationHolder.getApplication().getMainContext()
                    .getBean(GrailsAwareApplicationServlet.VAADIN_APPLICATION_BEAN_NAME);
        } catch (final BeansException e) {
            if (GrailsAwareGAEApplicationServlet.log.isInfoEnabled()) {
                GrailsAwareGAEApplicationServlet.log
                        .info("Unable to acquire new Vaadin Application instance from Spring application context.  Falling "
                                + "back to a vaadinApplicationClass.newInstance() call "
                                + "(note that this prevents dependency injection)...");
            }
            GrailsAwareGAEApplicationServlet.log.debug(
                    "Beans exception when attempting to acquire new Vaadin application instance from Spring:", e);
        }

        // should never execute, but just in case:
        if (app == null) {
            try {
                app = this.getApplicationClass().newInstance();
            } catch (final IllegalAccessException e) {
                throw new ServletException("getNewApplication failed", e);
            } catch (final InstantiationException e) {
                throw new ServletException("getNewApplication failed", e);
            } catch (final UnavailableClassException e) {
                throw new ServletException("getNewApplication failed", e);
            }
        }

        return app;
    }

    /**
     * Called by the servlet container to indicate to a servlet that the servlet
     * is being placed into service.
     * <p/>
     * This implementation caches the application class name (not the class
     * itself) to use to acquire the Application class from the Grails
     * classloader at runtime.
     * 
     * @param servletConfig
     *            the object containing the servlet's configuration and
     *            initialization parameters
     * @throws ServletException
     *             if an exception has occurred that interferes with the
     *             servlet's normal operation.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void init(final ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        // Gets the application class name
        final String applicationClassName = servletConfig.getInitParameter("application");
        if (applicationClassName == null) {
            throw new ServletException("The 'application' servlet init parameter was not specified.  This parameter "
                    + "must be specified with a value of a fully qualified class name of a class that extends "
                    + "com.vaadin.Application.");
        }

        this.applicationClassName = applicationClassName;
        // make sure it exists at startup:
        try {
            this.getApplicationClass();
        } catch (final UnavailableClassException e) {
            throw new ServletException("Failed to load application class: " + this.applicationClassName, e);
        }
    }

    // NOTE!!!
    // If you change this implementation, you _MUST_ manually copy-and-paste the
    // changes into the
    // the GrailsAwareApplicationServlet 'service' method implementation - the
    // logic is identical, but in a
    // different class hierarchy

    @Override
    protected void service(final HttpServletRequest httpReq, final HttpServletResponse response)
            throws ServletException, IOException {

        HttpServletRequest request = httpReq; // default the request to use to
                                              // be the method argument
        final String restartToken = RestartingApplicationHttpServletRequest.getRestartToken();

        if (restartToken != null) {
            // The plugin has detected a source code change, so verify that the
            // corresponding
            // session reflects the current restart token. If not, that means
            // that any application currently
            // associated with the session is stale and does not reflect the
            // Vaadin artefact source code change. So,
            // we'll need to guarantee that the Vaadin application (not Grails)
            // restarts:

            final HttpSession session = request.getSession();

            final String sessionRestartToken = (String) session
                    .getAttribute(RestartingApplicationHttpServletRequest.RESTART_TOKEN_SESSION_KEY);

            if ((sessionRestartToken == null) || !sessionRestartToken.equals(restartToken)) {
                // the current session doesn't reflect the up-to-date restart
                // token. Make sure the
                // 'restartApplication' parameter is artificially set:
                request = new RestartingApplicationHttpServletRequest(request);

                // ensure the session is updated with the new token:
                session.setAttribute(RestartingApplicationHttpServletRequest.RESTART_TOKEN_SESSION_KEY, restartToken);
            }

            // ensure the session is updated with the new token:
            session.setAttribute(RestartingApplicationHttpServletRequest.RESTART_TOKEN_SESSION_KEY, restartToken);
        }

        // now process either with the default request or a potential wrapped
        // one:
        super.service(request, response);
    }
}