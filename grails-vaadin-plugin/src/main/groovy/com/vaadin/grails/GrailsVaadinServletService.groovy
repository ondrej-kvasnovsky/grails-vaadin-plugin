package com.vaadin.grails

import com.vaadin.server.DeploymentConfiguration
import com.vaadin.server.VaadinServlet
import com.vaadin.server.VaadinServletService
import grails.util.Holders
import groovy.transform.CompileStatic

/**
 * @author Ondrej Kvasnovsky
 */
@CompileStatic
class GrailsVaadinServletService extends VaadinServletService {

    GrailsVaadinServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration) {
        super(servlet, deploymentConfiguration)
    }

    @Override
    ClassLoader getClassLoader() {
        Holders.grailsApplication.getClassLoader()
    }
}
