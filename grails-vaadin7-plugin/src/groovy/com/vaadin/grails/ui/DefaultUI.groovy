package com.vaadin.grails.ui

import com.vaadin.grails.Grails
import com.vaadin.navigator.Navigator
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.UI
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

/**
 * Default implementation for {@link com.vaadin.ui.UI}.
 *
 * @author Stephan Grundner
 */
class DefaultUI extends UI {

    private static final Logger log = Logger.getLogger(DefaultUI)

    @Autowired
    ApplicationContext applicationContext

    protected Navigator createNavigator(UI ui, Object container) {
        def beanName = Grails.getUniqueBeanName(Navigator)
        applicationContext.getBean(beanName, ui, container)
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        log.debug("Initializing UI of type [${current.getClass()}] with ID [${getUIId()}]")
        navigator = createNavigator(this, this)
    }
}
