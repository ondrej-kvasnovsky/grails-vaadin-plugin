package com.app

import com.vaadin.grails.navigator.Views
import com.vaadin.grails.ui.DefaultUI
import com.vaadin.grails.ui.VaadinUI
import com.vaadin.server.VaadinRequest

@VaadinUI(path = '/')
class MyUI extends DefaultUI {

    @Override
    protected void init(VaadinRequest r) {
        super.init(r)

        Views.enter(ItemView)
    }
}
