package com.vaadin.grails
import grails.util.Environment
import org.slf4j.Logger
import org.slf4j.LoggerFactory
/**
 * Manages user defined VaadinConfig class.
 *
 * @author Ondrej Kvasnovsky
 */
class VaadinConfiguration {

    private static final transient Logger log = LoggerFactory.getLogger(this)

    private static final String VAADIN_CONFIG_FILE = "VaadinConfig"

    ConfigObject configuration = null;

    private ClassLoader loader

    VaadinConfiguration(ClassLoader classLoader) {
        loader = classLoader
    }

    def getConfig() {
        if (configuration == null) {
            try {
                Class configFile = loader.loadClass(VAADIN_CONFIG_FILE);
                configuration = new ConfigSlurper(Environment.current.name).parse(configFile);
            } catch (ClassNotFoundException e) {
                log.warn "Unable to find Vaadin plugin config file: ${VAADIN_CONFIG_FILE}.groovy"
            }
        }
        def res = configuration?.vaadin
        return res;
    }
}
