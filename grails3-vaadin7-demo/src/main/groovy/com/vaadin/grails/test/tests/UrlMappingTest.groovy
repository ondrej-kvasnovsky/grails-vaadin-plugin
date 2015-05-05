package com.vaadin.grails.test.tests

import com.vaadin.grails.test.utils.PluginTest
import com.vaadin.grails.test.utils.PluginTestSuite
import org.springframework.boot.context.embedded.ServletRegistrationBean

import static com.vaadin.grails.Grails.get

@PluginTestSuite
class UrlMappingTest {

    @PluginTest
    public void 'UI is registered in servlet mappings'() {
        ServletRegistrationBean servlet = get('vaadinUIServlet')

        Map initParams = (Map) servlet.getProperties().get('initParameters')
        assert initParams.get('UI') == 'com.vaadin.grails.test.TestUI'

        Set<String> mappings = servlet.urlMappings
        assert mappings.toList() == ['/*', '/VAADIN/*']

        assert servlet.loadOnStartup == 1
    }
}
