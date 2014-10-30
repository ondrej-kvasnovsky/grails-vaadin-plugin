package com.vaadin.grails.navigator

import com.vaadin.navigator.Navigator
import com.vaadin.server.Page
import org.apache.log4j.Logger
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Primary
@Component("uriFragmentManager")
@Scope("prototype")
class DefaultUriFragmentManager extends Navigator.UriFragmentManager {

    private static final Logger log = Logger.getLogger(DefaultUriFragmentManager)

    DefaultUriFragmentManager(Page page) {
        super(page)
        log.debug("Uri fragment manager of type [${this.getClass()}] created")
    }
}
