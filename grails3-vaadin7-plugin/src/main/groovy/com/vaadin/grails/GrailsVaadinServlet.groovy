package com.vaadin.grails

import com.vaadin.server.VaadinServlet
import com.vaadin.server.DeploymentConfiguration
import com.vaadin.server.VaadinServletService

/**
 *
 *
 * @author Ondrej Kvasnovsky
 */
class GrailsVaadinServlet extends VaadinServlet {

    @Override
    protected DeploymentConfiguration createDeploymentConfiguration(Properties initParameters) {
        // TODO: http://dev.vaadin.com/ticket/9819 we shouldn't need our own GrailsVaadinServletService
        // initParameters.setProperty("ClassLoader", Holders.getApplicationContext().getClassLoader())
        def configuration = super.createDeploymentConfiguration(initParameters)
        return configuration
    }

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) {
        GrailsVaadinServletService service = new GrailsVaadinServletService(this, deploymentConfiguration)
        service.init()
        // TODO: use VaadinService.setClassLoader()
        return service
    }

}
