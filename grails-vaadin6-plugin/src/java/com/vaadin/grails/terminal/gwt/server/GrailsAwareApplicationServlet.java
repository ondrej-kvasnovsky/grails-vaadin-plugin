/*
 * Copyright 2010 Daniel Bell
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
package com.vaadin.grails.terminal.gwt.server;

import grails.util.Holders;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.ui.Root;

/**
 * @author Les Hazlewood
 * @author Daniel Bell
 * @author Ondrej Kvasnovsky
 * @author Jan Rudovsky
 */
@SuppressWarnings("serial")
public class GrailsAwareApplicationServlet extends AbstractApplicationServlet {

	public static final String VAADIN_APPLICATION_BEAN_NAME = "vaadinApplication";

	private static final transient Logger log = LoggerFactory
			.getLogger(GrailsAwareApplicationServlet.class);

	private Class<? extends Application> applicationClass;
	private Class<? extends Root> rootClass;

	/**
	 * Called by the servlet container to indicate to a servlet that the servlet
	 * is being placed into service.
	 * 
	 * @param servletConfig
	 *            the object containing the servlet's configuration and
	 *            initialization parameters
	 * @throws javax.servlet.ServletException
	 *             if an exception has occurred that interferes with the
	 *             servlet's normal operation.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init(javax.servlet.ServletConfig servletConfig)
			throws javax.servlet.ServletException {
		super.init(servletConfig);

		// Loads the application class using the same class loader
		// as the servlet itself

		try {
			applicationClass = ServletPortletHelper.getApplicationClass(
					servletConfig.getInitParameter("application"),
					servletConfig.getInitParameter(Application.ROOT_PARAMETER),
					getClassLoader());
			rootClass = (Class<? extends Root>) getClassLoader().loadClass(servletConfig.getInitParameter(Application.ROOT_PARAMETER));
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected Application getNewApplication(HttpServletRequest request)
			throws ServletException {
		// Creates a new application instance
		try {
			final Application application = getApplicationClass().newInstance();
			final Root root = rootClass.newInstance();
			root.setApplication(application);
			return application;
		} catch (final IllegalAccessException e) {
			throw new ServletException("getNewApplication failed", e);
		} catch (final InstantiationException e) {
			throw new ServletException("getNewApplication failed", e);
		} catch (ClassNotFoundException e) {
			throw new ServletException("getNewApplication failed", e);
		}
	}

	@Override
	protected Class<? extends Application> getApplicationClass()
			throws ClassNotFoundException {
		return applicationClass;
	}
	
	@Override
	protected ClassLoader getClassLoader() throws ServletException {
		ClassLoader classLoader = Holders.getGrailsApplication().getClassLoader();
		return classLoader;
	}
	
	@Override
    protected void service(HttpServletRequest httpReq, HttpServletResponse response) throws ServletException,
            IOException {

        HttpServletRequest request = httpReq; // default the request to use to
                                              // be the method argument
        String restartToken = RestartingApplicationHttpServletRequest.getRestartToken();

        if (restartToken != null) {
            // The plugin has detected a source code change, so verify that the
            // corresponding
            // session reflects the current restart token. If not, that means
            // that any application currently
            // associated with the session is stale and does not reflect the
            // Vaadin artefact source code change. So,
            // we'll need to guarantee that the Vaadin application (not Grails)
            // restarts:

            HttpSession session = request.getSession();

            String sessionRestartToken = (String) session
                    .getAttribute(RestartingApplicationHttpServletRequest.RESTART_TOKEN_SESSION_KEY);

            if (sessionRestartToken == null || !sessionRestartToken.equals(restartToken)) {
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
