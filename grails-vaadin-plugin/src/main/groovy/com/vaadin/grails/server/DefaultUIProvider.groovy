package com.vaadin.grails.server

import com.vaadin.grails.navigator.VaadinView
import com.vaadin.grails.ui.VaadinUI
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.ViewProvider
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.server.UIProvider
import com.vaadin.ui.UI
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation for {@link com.vaadin.server.UIProvider}.
 *
 * @author Stephan Grundner
 */
@CompileStatic
@Primary
@Component("uiProvider")
@Scope("prototype")
class DefaultUIProvider extends UIProvider {

    @Autowired
    ApplicationContext applicationContext

    final Map<String, Class<? extends UI>> typesByPaths = new ConcurrentHashMap()

    protected boolean hasViews(Class<? extends UI> uiClass) {
        applicationContext.getBeanNamesForAnnotation(VaadinView).find { beanName ->
            def registered = applicationContext.findAnnotationOnBean(beanName, VaadinView)
            registered.ui().find { it == uiClass }
        }
    }

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        typesByPaths[event.request.pathInfo]
    }

    protected void applyViewProvider(UI ui) {
        def navigator = new Navigator(ui, ui)
        navigator.addProvider(applicationContext.getBean(ViewProvider))
        ui.navigator = navigator
    }

    @Override
    UI createInstance(UICreateEvent event) {
        def type = event.getUIClass()
        def ui = applicationContext.getBean(type)

        if (hasViews(type)) {
            applyViewProvider(ui)
        }

        ui
    }

    @PostConstruct
    void init() {
        for (beanName in applicationContext.getBeanNamesForAnnotation(VaadinUI)) {
            String path = applicationContext.findAnnotationOnBean(beanName, VaadinUI).path()
            typesByPaths[path] = (Class<? extends UI>) applicationContext.getType(beanName)
        }
    }
}
