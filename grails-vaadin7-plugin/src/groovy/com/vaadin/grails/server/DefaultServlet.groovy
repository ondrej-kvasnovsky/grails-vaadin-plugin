package com.vaadin.grails.server

import com.vaadin.grails.Grails
import com.vaadin.server.SessionInitListener
import com.vaadin.server.VaadinServlet
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
class DefaultServlet extends VaadinServlet {

//    private static final Logger log = Logger.getLogger(DefaultServlet)

    ApplicationContext getApplicationContext() {
        Grails.applicationContext
    }

    @Override
    protected void servletInitialized() throws ServletException {
        def sessionInitListener = applicationContext.getBean(SessionInitListener)
        service.addSessionInitListener(sessionInitListener)
    }

    protected void withNewSession(Closure<Void> closure) {
        def datastore = applicationContext.getBean("hibernateDatastore") as Datastore
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
        withNewSession() {
            super.service(request, response)
        }
    }
}
