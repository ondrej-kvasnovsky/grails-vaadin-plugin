package com.app

import com.vaadin.grails.navigator.VaadinView
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import grails.core.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired

import static com.vaadin.grails.Grails.i18n

@VaadinView(path = "item")
class ItemView extends VerticalLayout implements View {

    @Autowired
    private GrailsApplication grailsApplication

    @Override
    void enter(ViewChangeListener.ViewChangeEvent e) {
        setMargin(true)

        String homeLabel = i18n("default.home.label")
        Label label = new Label(homeLabel)
        addComponent(label)

        String appVersion = grailsApplication.config.info.app.version
        Label appVersionLabel = new Label(appVersion)
        addComponent(appVersionLabel)
    }
}