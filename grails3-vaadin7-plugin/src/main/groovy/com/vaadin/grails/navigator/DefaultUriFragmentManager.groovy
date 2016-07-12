package com.vaadin.grails.navigator

import com.vaadin.navigator.Navigator
import com.vaadin.server.Page
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Default implementation for {@link com.vaadin.navigator.Navigator.UriFragmentManager}.
 *
 * @author Stephan Grundner
 */
@CompileStatic
@Primary
@Component("uriFragmentManager")
@Scope("prototype")
@Slf4j
class DefaultUriFragmentManager extends Navigator.UriFragmentManager {

    DefaultUriFragmentManager(Page page) {
        super(page)
        log.debug('Uri fragment manager of type [{}] created', getClass().name)
    }
}
