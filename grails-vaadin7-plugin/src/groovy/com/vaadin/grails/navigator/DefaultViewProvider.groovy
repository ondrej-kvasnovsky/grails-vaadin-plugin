package com.vaadin.grails.navigator

import com.vaadin.navigator.View
import com.vaadin.navigator.ViewProvider
import com.vaadin.ui.UI
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Default implementation for {@link com.vaadin.navigator.ViewProvider}.
 *
 * @author Stephan Grundner
 */
@Component("viewProvider")
@Scope("prototype")
class DefaultViewProvider implements ViewProvider {

    private static final Logger log = Logger.getLogger(DefaultViewProvider)

    @Autowired
    ApplicationContext applicationContext

    final Map<String, String> beanNamesByPaths = new HashMap()

    DefaultViewProvider() {
        log.debug("ViewProvider of type [${this.getClass()}] created")
    }

    protected boolean built

    protected boolean belongsToCurrentUI(VaadinView registered) {
        def uis = registered.ui()
        if (uis.size() > 0) {
            return uis.contains(UI.current.class)
        }
        true
    }

    @PostConstruct
    void init() {
//        Cannot be used to build the map, because current UI ist not initialized yet!
    }

    /**
     * Build the view map.
     *
     * @return True if at least one annotated view was found, otherwise false
     */
    protected boolean build() {
        beanNamesByPaths.clear()
        boolean found = false
        def beanNames = applicationContext.getBeanNamesForAnnotation(VaadinView)
        beanNames.each { beanName ->
            VaadinView registered = applicationContext
                    .findAnnotationOnBean(beanName, VaadinView)
            if (belongsToCurrentUI(registered)) {
                def path = registered.path()
                beanNamesByPaths.put(path, beanName)
                def type = applicationContext.getType(beanName)
                log.debug("Registered view [${type}] with path [${path}] and ui [${UI.current.class}]")
                found = true
            }
        }
        built = true
        found
    }

    protected String getBeanName(String path) {
        beanNamesByPaths.get(path)
    }

    @Override
    String getViewName(String viewAndParameters) {
        if (!built) {
            build()
        }
        String path = viewAndParameters
        while (true) {
            if (beanNamesByPaths.containsKey(path)) {
                return path
            }
            def i = path.lastIndexOf("/")
            if (i == -1) {
                break
            }
            path = path.substring(0, i)
        }
        viewAndParameters
    }


    View getView(String path) {
        def beanName = getBeanName(path)
        log.debug("Got bean name [${beanName}] for path [${path}]")
        if (beanName) {
            return applicationContext.getBean(beanName)
        }
        null
    }
}
