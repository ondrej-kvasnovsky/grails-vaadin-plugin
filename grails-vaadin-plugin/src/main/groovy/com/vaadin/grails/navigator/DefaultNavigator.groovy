package com.vaadin.grails.navigator

import com.vaadin.grails.Grails
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.ViewDisplay
import com.vaadin.navigator.ViewProvider
import com.vaadin.server.Page
import com.vaadin.ui.ComponentContainer
import com.vaadin.ui.SingleComponentContainer
import com.vaadin.ui.UI
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Default implementation for {@link com.vaadin.navigator.Navigator}.
 *
 * @author Stephan Grundner
 */
@CompileStatic
@Primary
@Component("navigator")
@Scope("prototype")
@Slf4j
class DefaultNavigator extends Navigator {

    DefaultNavigator(UI ui, ComponentContainer container) {
        this(ui, new Navigator.ComponentContainerViewDisplay(container))
    }

    DefaultNavigator(UI ui, SingleComponentContainer container) {
        this(ui, new Navigator.SingleComponentContainerViewDisplay(container))
    }

    DefaultNavigator(UI ui, ViewDisplay display) {
        super(ui, createUriFragmentManager(ui.page), display)
        addProvider(createViewProvider())
        log.debug('Navigator of type [{}] created', getClass().name)
    }

    private static Navigator.UriFragmentManager createUriFragmentManager(Page page) {
        def beanName = Grails.getUniqueBeanName(Navigator.UriFragmentManager)
        (Navigator.UriFragmentManager) Grails.applicationContext.getBean(beanName, page)
    }

    protected ViewProvider createViewProvider() {
        Grails.applicationContext.getBean(ViewProvider)
    }
}
