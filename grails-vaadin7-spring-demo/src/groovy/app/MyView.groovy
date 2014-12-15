package app

import com.vaadin.grails.navigator.VaadinView
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

@VaadinView(path = "my")
class MyView extends VerticalLayout implements View {

    @Override
    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        addComponent(new Label('my view'))
    }
}
