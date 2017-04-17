package com.vaadin.grails.ui

import com.vaadin.grails.Grails
import com.vaadin.navigator.Navigator
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.UI
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

/**
 * Default implementation for {@link com.vaadin.ui.UI}.
 *
 * @author Stephan Grundner
 */
@CompileStatic
@Slf4j
class DefaultUI extends UI {

    @Autowired
    ApplicationContext applicationContext

    protected Navigator createNavigator(UI ui, Object container) {
        def beanName = Grails.getUniqueBeanName(Navigator)
        (Navigator) applicationContext.getBean(beanName, ui, container)
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        navigator = createNavigator(this, this)
    }
}
