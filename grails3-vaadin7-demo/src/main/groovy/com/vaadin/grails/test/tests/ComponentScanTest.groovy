package com.vaadin.grails.test.tests

import com.component.scan.test.TestBean
import com.vaadin.grails.test.utils.PluginTest
import com.vaadin.grails.test.utils.PluginTestSuite

import static com.vaadin.grails.Grails.get

@PluginTestSuite
class ComponentScanTest {

    @PluginTest
    public void 'Bean is present in the application context'() {
        TestBean bean = get('testBean')
        assert bean != null
    }
}
