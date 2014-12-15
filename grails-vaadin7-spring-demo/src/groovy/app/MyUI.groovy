package app
import com.vaadin.grails.navigator.Views
import com.vaadin.grails.ui.DefaultUI
import com.vaadin.grails.ui.VaadinUI
import com.vaadin.server.VaadinRequest

@VaadinUI(path = '/')
class MyUI extends DefaultUI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        super.init(vaadinRequest)

        Views.enter(MyView)
    }
}
