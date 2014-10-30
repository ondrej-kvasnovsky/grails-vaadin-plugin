package com.vaadin.grails.server

import com.vaadin.server.ServiceException
import com.vaadin.server.SessionInitEvent
import com.vaadin.server.SessionInitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Primary
@Component("sessionInitListener")
@Scope("prototype")
class DefaultSessionInitListener implements SessionInitListener {

    @Autowired
    ApplicationContext applicationContext

    @Override
    void sessionInit(SessionInitEvent event) throws ServiceException {
        event.session.addUIProvider(applicationContext.getBean(DefaultUIProvider))
    }
}
