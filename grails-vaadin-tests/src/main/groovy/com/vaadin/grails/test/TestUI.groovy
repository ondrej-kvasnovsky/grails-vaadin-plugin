package com.vaadin.grails.test

import com.vaadin.grails.test.tests.PluginTestRunner
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout

class TestUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        VerticalLayout layout = new VerticalLayout()
        layout.setMargin(true)
        setContent(layout)

        new PluginTestRunner().run()
    }
}
