package com.vaadin.grails.server

import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UICreateEvent
import com.vaadin.server.UIProvider
import com.vaadin.shared.communication.PushMode
import com.vaadin.shared.ui.ui.Transport
import com.vaadin.ui.UI
import grails.util.Holders

/**
 * Dispatcher <code>UIProvider</code>.
 *
 * @author Stephan Grundner
 */
final class DispatcherUIProvider extends UIProvider {

    private UIProvider delegate

    @Override
    Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        if (delegate == null) {
            delegate = Holders.applicationContext.getBean(UIProvider)
        }
        return delegate.getUIClass(event)
    }

    @Override
    UI createInstance(UICreateEvent event) {
        delegate.createInstance(event)
    }

    @Override
    String getTheme(UICreateEvent event) {
        delegate.getTheme(event)
    }

    @Override
    String getWidgetset(UICreateEvent event) {
        delegate.getWidgetset(event)
    }

    @Override
    boolean isPreservedOnRefresh(UICreateEvent event) {
        delegate.isPreservedOnRefresh(event)
    }

    @Override
    String getPageTitle(UICreateEvent event) {
        delegate.getPageTitle(event)
    }

    @Override
    PushMode getPushMode(UICreateEvent event) {
        delegate.getPushMode(event)
    }

    @Override
    Transport getPushTransport(UICreateEvent event) {
        delegate.getPushTransport(event)
    }
}