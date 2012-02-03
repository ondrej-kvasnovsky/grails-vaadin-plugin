/*
 * Copyright 2008 Les Hazlewood
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
 */
package com.vaadin.terminal.gwt.server;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * An {@code HttpServletRequest} wrapper implementation that guarantees the
 * existence of a {@link Constants#URL_PARAMETER_RESTART_APPLICATION
 * restartApplication} request parameter by artificially setting it with a value
 * of {@code "true"}.
 * <p/>
 * When the Grails Vaadin plugin determines that a Vaadin component's source
 * code has changed, it will create an instance of this class to use when
 * calling the Vaadin {@link AbstractApplicationServlet
 * AbstractApplicationServlet} that processes all Vaadin requests. This servlet
 * will react to the artificially-set {@code restartApplication} request
 * parameter and automatically reload a new Vaadin
 * {@link com.vaadin.Application Application} instance that reflects the changed
 * source code.
 * <p/>
 * Note: this class makes strict assumptions of Vaadin's internal implementation
 * details. It is not intended to be used by end-users directly.
 * 
 * @author Les Hazlewood
 * @since 1.2
 */
public class RestartingApplicationHttpServletRequest extends HttpServletRequestWrapper {

    public static final String RESTART_TOKEN_SESSION_KEY = RestartingApplicationHttpServletRequest.class.getName()
            + ".RESTART_TOKEN_SESSION_KEY";

    private static String restartToken;

    /**
     * Returns a token value used to determine if a request's associated Vaadin
     * application needs to be reloaded. The token-based logic to support
     * reloading functions as follows:
     * <ol>
     * <li>If this method returns {@code null}, no source code has changed and
     * the request should be processed as normal.</li>
     * <li>If this method returns a non-{@code null} value, the Application
     * source code has changed:
     * <ol>
     * <li>If the request's
     * {@link javax.servlet.http.HttpServletRequest#getSession() session} has
     * this exact token value stored as a session
     * {@link javax.servlet.http.HttpSession#getAttribute(String key) attribute}
     * , then the Application has already been reloaded for that user session
     * and nothing needs to be done.<br/>
     * <br/>
     * </li>
     * <li>If the request does not yet have a session or the session does not
     * reference this exact token value, the Vaadin application associated with
     * the session needs to be reloaded. <br/>
     * <br/>
     * In this case, a {@code ReloadingHttpServletRequest} will be created to
     * wrap the original request, but it will additionally impersonating a
     * {@link Constants#URL_PARAMETER_RESTART_APPLICATION restartApplication}
     * request parameter as being set to {@code true}. <br/>
     * <br/>
     * When the Vaadin {@link AbstractApplicationServlet
     * AbstractApplicationServlet}
     * {@link AbstractApplicationServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     * services} the request, it will look at this request parameter and reload
     * the application to reflect the changed Groovy Application class.</li>
     * </ol>
     * </li>
     * </ol>
     * 
     * @return a token value used to determine if a request's associated Vaadin
     *         application needs to be reloaded.
     */
    public static String getRestartToken() {
        return RestartingApplicationHttpServletRequest.restartToken;
    }

    /**
     * Sets a unique token value used to trigger new Vaadin Application
     * instances to be dynamically recreated after a Grails Vaadin component
     * source-code change. Read the {@link #getRestartToken() getRestartToken()}
     * JavaDoc to understand how the token influences Application reload.
     * 
     * @param token
     *            the unique token value used to trigger new Vaadin Application
     *            instances to be dynamically recreated after a Grails Vaadin
     *            component source-code change.
     */
    public static void setRestartToken(final String token) {
        RestartingApplicationHttpServletRequest.restartToken = token;
    }

    private final Map<String, String[]> parameterMap;

    public RestartingApplicationHttpServletRequest(final HttpServletRequest request) {
        super(request);
        final Map<String, String[]> paramMap = request.getParameterMap();
        if ((paramMap == null) || paramMap.isEmpty()) {
            this.parameterMap = new LinkedHashMap<String, String[]>(1);
        } else {
            this.parameterMap = new LinkedHashMap<String, String[]>(paramMap.size() + 1);

        }
        // ensure this exists to guarantee an application reload:
        this.parameterMap.put(Constants.URL_PARAMETER_RESTART_APPLICATION, new String[] { "true" });
    }

    @Override
    public String getParameter(final String name) {
        final String[] values = this.parameterMap.get(name);
        if ((values == null) || (values.length == 0)) {
            return null;
        }
        return values[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return new LinkedHashMap<String, String[]>(this.parameterMap);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        final Iterator<String> iter = new LinkedHashSet<String>(this.parameterMap.keySet()).iterator();
        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return iter.hasNext();
            }

            @Override
            public String nextElement() {
                return iter.next();
            }
        };
    }

    @Override
    public String[] getParameterValues(final String name) {
        final String[] values = this.parameterMap.get(name);
        if ((values == null) || (values.length == 0)) {
            return null;
        }
        final String[] copy = new String[values.length];
        System.arraycopy(values, 0, copy, 0, values.length);
        return copy;
    }
}
