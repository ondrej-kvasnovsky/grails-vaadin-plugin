package com.vaadin.grails

import grails.util.Environment
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

/**
 * Manages user defined VaadinConfig class.
 *
 * @author Ondrej Kvasnovsky
 */
@CompileStatic
@Slf4j
class VaadinConfiguration {

    private static final String VAADIN_CONFIG_FILE = "VaadinConfig"

    private ConfigObject configuration

    private ClassLoader loader

    VaadinConfiguration(ClassLoader classLoader) {
        loader = classLoader
    }

    def getConfig() {
        if (configuration == null) {
            try {
                Class configFile = loader.loadClass(VAADIN_CONFIG_FILE)
                configuration = new ConfigSlurper(Environment.current.name).parse(configFile)
            } catch (ClassNotFoundException e) {
                log.warn 'Unable to find Vaadin plugin config file: {}.groovy', VAADIN_CONFIG_FILE
            }
        }
        configuration?.vaadin
    }
}
