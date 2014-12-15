package app

import com.vaadin.grails.navigator.Views
import com.vaadin.grails.ui.VaadinUI
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.UI

@VaadinUI(path = "/demo")
class DemoUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Views.enter(DemoView)
    }
}
