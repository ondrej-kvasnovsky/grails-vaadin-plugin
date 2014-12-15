package app

import com.vaadin.grails.navigator.VaadinView
import com.vaadin.grails.navigator.Views
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.Page
import com.vaadin.ui.Button
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout

@VaadinView(path = "demo2", ui = DemoUI)
class Demo2View extends CustomComponent implements View {

    @Override
    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        def layout = new VerticalLayout()
        layout.setMargin(true)
        layout.setSpacing(true)
        layout.addComponent(new Label(value: "View Nr. 2", styleName: "h1"))
        def paramField = new TextField("Some parameter")
        layout.addComponent(paramField)
        layout.addComponent(new Button("Go back", new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                Views.enter(DemoView, [someparam: paramField.value])
            }
        }))
        compositionRoot = layout

        Page.current.setTitle("Demo View Nr. 2")
    }
}