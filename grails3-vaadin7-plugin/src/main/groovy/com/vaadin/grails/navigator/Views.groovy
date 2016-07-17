package com.vaadin.grails.navigator

import com.vaadin.grails.Grails
import com.vaadin.grails.ui.VaadinUI
import com.vaadin.navigator.View
import com.vaadin.server.Page
import com.vaadin.ui.UI
import groovy.transform.CompileStatic
import org.grails.web.util.WebUtils
import org.springframework.util.Assert

/**
 * Ease navigation between Views.
 *
 * @author Stephan grundner
 */
@CompileStatic
final class Views {

    /**
     * Encode a map to a String.
     *
     * @param params A parameter map
     * @return A string representation of a map
     */
    static String encodeParams(Map params) {
        def encoded = WebUtils.toQueryString(params).replace("&", "/")
        if (encoded.startsWith("?")) {
            encoded = encoded.substring(1)
        }
        encoded
    }

    /**
     * Decode a String to a map.
     *
     * @param encoded An encoded String
     * @return A map representation of an encoded String
     */
    static Map decodeParams(String encoded) {
        WebUtils.fromQueryString(encoded.replace("/", "&"))
    }

    /**
     * Enter a view (in the current ui).
     *
     * @param path The path of the view
     * @param params The parameter map (optional)
     */
    static void enter(String path, Map params = null) {
        UI.current.navigator.navigateTo(params ? path + '/' + encodeParams(params) : path)
    }

    /**
     * Enter a view (in the current ui).
     *
     * @param type The type of the view (must be annotated with @GraadinView)
     * @param params The parameter map (optional)
     */
    static void enter(Class<? extends View> type, Map params = null) {
        def viewBeanName = Grails.getUniqueBeanName(type)
        Assert.notNull(viewBeanName, "No View found for type [${type}]")
        VaadinView view = Grails.applicationContext.findAnnotationOnBean(viewBeanName, VaadinView)
        enter(view.path(), params)
    }

    /**
     * Enter a view in a different ui.
     *
     * @param uiType The type of the ui (must be annotated with @GraadinUI)
     * @param viewType The type of the view (must be annotated with @GraadinView)
     * @param params The parameter map (optional)
     */
    static void enter(Class<? extends UI> uiType, Class<? extends View> viewType, Map params = null) {
        def applicationContext = Grails.applicationContext

        String beanName = Grails.getUniqueBeanName(uiType)
        Assert.notNull(beanName, "No UI found for type [${uiType}]")
        VaadinUI ui = applicationContext.findAnnotationOnBean(beanName, VaadinUI)

        beanName = Grails.getUniqueBeanName(viewType)
        Assert.notNull(beanName, "No View found for type [${viewType}]")
        VaadinView view = applicationContext.findAnnotationOnBean(beanName, VaadinView)

        String location = "${ui.path()}#!${view.path()}"
        Page.current.setLocation(params ? location + '/' + encodeParams(params) : location)
    }

    private Views() {}
}
