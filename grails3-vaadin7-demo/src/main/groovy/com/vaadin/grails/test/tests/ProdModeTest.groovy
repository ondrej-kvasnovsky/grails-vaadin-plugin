package com.vaadin.grails.test.tests

import com.vaadin.grails.test.utils.PluginTest
import com.vaadin.grails.test.utils.PluginTestSuite

import javax.servlet.ServletContext

import static com.vaadin.grails.Grails.get

@PluginTestSuite
class ProdModeTest {

    @PluginTest
    public void 'Production mode is disabled'() {
        ServletContext servlet = get(ServletContext)
        assert servlet.getInitParameter('productionMode') == 'false'
    }
}
