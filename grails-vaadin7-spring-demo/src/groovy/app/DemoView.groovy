package app

import com.vaadin.grails.navigator.VaadinView
import com.vaadin.grails.navigator.Views
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.Page
import com.vaadin.ui.Button
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

@VaadinView(path = "demo", ui = DemoUI)
class DemoView extends CustomComponent implements View {

    @Override
    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        VerticalLayout layout = new VerticalLayout()
        layout.setMargin(true)
        layout.setSpacing(true)
        layout.addComponent(new Label(value: "View Nr. 1", styleName: "h1"))
        layout.addComponent(new Button("Go forward", new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                Views.enter(Demo2View)
            }
        }))
        compositionRoot = layout

        Page.current.setTitle("Demo View Nr. 1")
    }
}