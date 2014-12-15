package app
import com.vaadin.grails.ui.DefaultUI
import com.vaadin.grails.ui.VaadinUI
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

@VaadinUI(path = '/admin')
class AdminUI extends DefaultUI {

    protected void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout()
        layout.setMargin(true)

        Label label = new Label("Admin")
        layout.addComponent(label)

        setContent(layout)
    }
}