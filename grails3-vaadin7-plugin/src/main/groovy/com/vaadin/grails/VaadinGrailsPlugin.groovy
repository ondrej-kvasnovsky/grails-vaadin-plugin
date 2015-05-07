package com.vaadin.grails

import grails.util.Holders
import org.springframework.boot.context.embedded.FilterRegistrationBean
import org.springframework.boot.context.embedded.InitParameterConfiguringServletContextInitializer
import org.springframework.boot.context.embedded.ServletRegistrationBean
import org.springframework.core.Ordered

/**
 * Plugin that integrates Grails with Vaadin 7.
 *
 * @author Ondrej Kvasnovsky
 */
class VaadinGrailsPlugin {

//    private static final String DEFAULT_SERVLET = "com.vaadin.grails.GrailsVaadinServlet";
    private static final String DEFAULT_SERVLET = "com.vaadin.grails.server.DefaultServlet"

    def version = "7.4.3"
    def grailsVersion = "3.0 > *"
    def pluginExcludes = []

    def title = "Vaadin 7 Plugin"
    def author = "Ondrej Kvasnovsky"
    def authorEmail = "ondrej.kvasnovsky@gmail.com"
    def description = '''
        Grails plugin integrating Vaadin 7 into the Grails project.
        '''

    def documentation = "http://vaadinongrails.com"
    def license = "APACHE"
    def organization = [name: "Ondrej Kvasnovsky", url: "http://vaadinongrails.com"]
    def issueManagement = [system: "JIRA", url: "http://jira.grails.org/browse/GPVAADIN"]
    def scm = [url: "https://github.com/ondrej-kvasnovsky/grails-vaadin-plugin"]

    VaadinConfiguration vaadinConfiguration

    def doWithSpring = {
        vaadinConfiguration = new VaadinConfiguration(Holders.getGrailsApplication().classLoader)

        Map config = vaadinConfiguration.getConfig()
        if (!config) {
            return
        }

        Map mapping = config.mapping
        boolean usingUIProvider = false

        if (mapping.isEmpty()) {
            def uiProvider = config.uiProvider ?: 'com.vaadin.grails.server.DispatcherUIProvider'
            mapping = ["/*": uiProvider]
            usingUIProvider = true
        }

        def applicationServlet = config.servletClass ?: DEFAULT_SERVLET
        def widgetset = config.widgetset
        boolean asyncSupportedValue = config.asyncSupported
        Map initParams = config.initParams // TODO: write test for initParams in config
        Class clazz = getClass().getClassLoader().loadClass(applicationServlet)

        mapping.eachWithIndex() { obj, i ->
            Map params = [:]
            if (usingUIProvider) {
                params.put('UIProvider', obj.value)
            } else {
                params.put('UI', obj.value)
            }

            if (asyncSupportedValue) {
                params.put('pushmode', 'automatic')
            }

            if (widgetset) { // TODO: use a widget and write test
                params.put('widgetset', widgetset)
            }

            for (def name : initParams?.keySet()) {
                params.put(name, initParams.get(name))
            }

            List mappings
            if (i == 0) {
                mappings = [obj.key, '/VAADIN/*']
            } else {
                mappings = [obj.key]
            }

            "vaadinUIServlet${i}"(ServletRegistrationBean, clazz.newInstance(), obj.key) {
                name = "vaadinUIServlet${i}"
                initParameters = params
                asyncSupported = asyncSupportedValue
                loadOnStartup = 1
                urlMappings = mappings
            }
        }

        def osivFilterClassName = config.openSessionInViewFilter

        if (osivFilterClassName) {
            Class osiv = getClass().getClassLoader().loadClass(osivFilterClassName)
            osivFilter(FilterRegistrationBean) {
                filter = osiv.newInstance()
                urlPatterns = ['/*']
                order = Ordered.HIGHEST_PRECEDENCE
            }
        }

        def vaadinProductionMode = config.productionMode
        Map prodModeParams = ["productionMode": vaadinProductionMode]
        prodModeServletContextInitializer(InitParameterConfiguringServletContextInitializer, prodModeParams)

        def packages = config.packages ?: ['*']
        xmlns([ctx: 'http://www.springframework.org/schema/context'])
        packages << 'com.vaadin.grails'
        String packagesAsString = packages.join(',')
        ctx.'component-scan'('base-package': packagesAsString)
    }

}
