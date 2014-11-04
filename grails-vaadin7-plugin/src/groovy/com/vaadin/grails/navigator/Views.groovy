package com.vaadin.grails.navigator

import com.vaadin.grails.Grails
import com.vaadin.grails.ui.VaadinUI
import com.vaadin.navigator.View
import com.vaadin.server.Page
import com.vaadin.ui.UI
import org.codehaus.groovy.grails.web.util.WebUtils

/**
 * Ease navigation between Views.
 *
 * @author Stephan grundner
 */
final class Views {

    /**
     * Encode a map to a String.
     *
     * @param params A parameter map
     * @return A string representation of a map
     */
    static String encodeParams(Map params) {
        def encoded = WebUtils.toQueryString(params)
        encoded = encoded.replace("&", "/")
        if (encoded.startsWith("?")) {
            encoded = encoded.substring(1, encoded.length())
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
        def navigationState = path
        if (params && params.size() > 0) {
            def encoded = encodeParams(params)
            navigationState = "${path}/${encoded}"
        }
        UI.current.navigator.navigateTo(navigationState)
    }

    /**
     * Enter a view (in the current ui).
     *
     * @param type The type of the view (must be annotated with @GraadinView)
     * @param params The parameter map (optional)
     */
    static void enter(Class<? extends View> type, Map params = null) {
        def applicationContext = Grails.applicationContext
        def viewBeanName = Grails.getUniqueBeanName(type)
        if (viewBeanName == null) {
            throw new RuntimeException("No View found for type [${type}]")
        }
        def view = applicationContext.findAnnotationOnBean(viewBeanName, VaadinView)
        enter((String) view.path(), params)
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
        def uiBeanName = Grails.getUniqueBeanName(uiType)
        if (uiBeanName == null) {
            throw new RuntimeException("No UI found for type [${uiType}]")
        }
        def ui = applicationContext.findAnnotationOnBean(uiBeanName, VaadinUI)

        def viewBeanName = Grails.getUniqueBeanName(viewType)
        if (viewBeanName == null) {
            throw new RuntimeException("No View found for type [${viewType}]")
        }
        def view = applicationContext.findAnnotationOnBean(viewBeanName, VaadinView)

        def location = "${ui.path()}#!${view.path()}"

        if (params) {
            location += "/${encodeParams(params)}"
        }

        Page.current.setLocation(location)
    }

    private Views() { }
}
