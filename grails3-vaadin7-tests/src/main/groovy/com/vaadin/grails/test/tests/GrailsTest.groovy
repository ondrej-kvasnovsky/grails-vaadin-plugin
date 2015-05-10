package com.vaadin.grails.test.tests

import com.vaadin.grails.Grails
import com.vaadin.grails.test.UserService
import com.vaadin.grails.test.utils.PluginTest
import com.vaadin.grails.test.utils.PluginTestSuite
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource

@PluginTestSuite
public class GrailsTest {

    @PluginTest
    public void 'Returns localized text using i18n'() {
        String homeLabel = Grails.i18n("default.home.label")
        assert "Home" == homeLabel
    }

    @PluginTest
    public void 'Returns service bean by class from context'() {
        UserService service = Grails.get(UserService)
        assert service != null
    }

    @PluginTest
    public void 'Returns service bean by name from context'() {
        UserService service = Grails.get('userService')
        assert service != null
    }

    @PluginTest
    public void 'Returns application context'() {
        ApplicationContext context = Grails.applicationContext
        assert context != null
    }

    @PluginTest
    public void 'Returns message source'() {
        MessageSource messageSource = Grails.messageSource
        assert messageSource != null
    }

}
