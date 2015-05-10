package com.vaadin.grails.test.tests

import com.vaadin.grails.test.utils.PluginTest
import com.vaadin.grails.test.utils.PluginTestSuite
import org.springframework.boot.context.embedded.ServletRegistrationBean

import static com.vaadin.grails.Grails.get

@PluginTestSuite
class AsyncTest {

    @PluginTest
    public void 'Async is enabled'() {
        ServletRegistrationBean servlet = get('vaadinUIServlet0')
        assert servlet.asyncSupported

        Map initParams = (Map) servlet.getProperties().get('initParameters')
        assert initParams.get('pushmode') == 'automatic'
    }
}
