package com.vaadin.grails.test.tests

import com.vaadin.grails.test.utils.PluginTest
import com.vaadin.grails.test.utils.PluginTestSuite
import org.springframework.boot.context.embedded.ServletRegistrationBean

import static com.vaadin.grails.Grails.get

@PluginTestSuite
class UrlMappingTest {

    @PluginTest
    public void 'UI is registered in servlet mappings'() {
        ServletRegistrationBean servlet = get('vaadinUIServlet0')

        Map initParams = (Map) servlet.getProperties().get('initParameters')
        assert initParams.get('UI') == 'com.vaadin.grails.test.TestUI'

        Set<String> mappings = servlet.urlMappings
        assert mappings.toList() == ['/test/*', '/VAADIN/*']

        assert servlet.loadOnStartup == 1
    }

    @PluginTest
    public void 'Second UI is registered in servlet mappings'() {
        ServletRegistrationBean servlet = get('vaadinUIServlet1')

        Map initParams = (Map) servlet.getProperties().get('initParameters')
        assert initParams.get('UI') == 'com.vaadin.grails.test.DemoUI'

        Set<String> mappings = servlet.urlMappings
        assert mappings.toList() == ['/demo/*']

        assert servlet.loadOnStartup == 1
    }
}
