package com.vaadin.grails.server

import com.vaadin.grails.navigator.VaadinView
import com.vaadin.grails.ui.VaadinUI
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.ViewProvider
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.server.UIProvider
import com.vaadin.ui.UI
import org.apache.log4j.Logger
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
@Primary
@Component("uiProvider")
@Scope("prototype")
class DefaultUIProvider extends UIProvider {

    private static final Logger log = Logger.getLogger(DefaultUIProvider)

    @Autowired
    ApplicationContext applicationContext

    final Map<String, Class<? extends UI>> typesByPaths = new ConcurrentHashMap()

    protected boolean hasViews(Class<? extends UI> uiClass) {
        def beanNames = applicationContext.getBeanNamesForAnnotation(VaadinView)
        beanNames.find { beanName ->
            def registered = applicationContext.findAnnotationOnBean(beanName, VaadinView)
            registered.ui().find { it == uiClass }
        }
    }

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        String path = event.request.pathInfo
        typesByPaths.get(path)
    }

    protected void applyViewProvider(UI ui) {
        def navigator = new Navigator(ui, ui)
        def viewProvider = applicationContext.getBean(ViewProvider)
        navigator.addProvider(viewProvider)
        ui.navigator = navigator
    }

    @Override
    UI createInstance(UICreateEvent event) {
        def type = event.getUIClass()
        log.debug("Creating UI of type [${type}]")
        def ui = applicationContext.getBean(type)

        if (hasViews(type)) {
            applyViewProvider(ui)
        }

        ui
    }

    @PostConstruct
    void init() {
        def beanNames = applicationContext.getBeanNamesForAnnotation(VaadinUI)
        beanNames.each { beanName ->
            def registered = applicationContext.findAnnotationOnBean(beanName, VaadinUI)
            def type = applicationContext.getType(beanName)
            def path = registered.path()
            typesByPaths.put(path, type)
            log.debug("Registered UI of type [${type}] with path [${path}]")
        }
    }
}
