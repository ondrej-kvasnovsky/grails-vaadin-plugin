package com.vaadin.grails.server

import com.vaadin.grails.Grails
import com.vaadin.server.SessionInitListener
import com.vaadin.server.VaadinServlet
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.grails.datastore.mapping.core.Datastore
import org.grails.datastore.mapping.core.DatastoreUtils
import org.springframework.context.ApplicationContext

import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

/**
 * Default implementation for {@link com.vaadin.server.VaadinServlet}.
 *
 * @author Stephan Grundner
 */
@CompileStatic
@Slf4j
class DefaultServlet extends VaadinServlet {

    ApplicationContext getApplicationContext() {
        Grails.applicationContext
    }

    @Override
    protected void servletInitialized() throws ServletException {
        service.addSessionInitListener(applicationContext.getBean(SessionInitListener))
    }

    protected void withNewSession(Closure<Void> closure) {
        def datastore = applicationContext.getBean("hibernateDatastore", Datastore)
        def session = datastore.connect()
        DatastoreUtils.bindNewSession session
        try {
            closure.call(session)
        } finally {
            DatastoreUtils.unbindSession session
        }
    }

    @Override
    void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        withNewSession {
            super.service(request, response)
        }
    }
}
