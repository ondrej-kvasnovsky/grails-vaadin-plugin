package com.vaadin.grails.test

import com.vaadin.server.VaadinRequest
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout

class SecondUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layout = new VerticalLayout()
        layout.setMargin(true)

        layout.addComponent(new Label("Second UI test label"))
        setContent(layout)
    }
}
