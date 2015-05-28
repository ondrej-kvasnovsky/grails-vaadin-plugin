package app

import com.vaadin.annotations.Theme
import com.vaadin.grails.Grails
import com.vaadin.grails.ui.VaadinUI
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import grails.vaadin7.demo.User
import grails.vaadin7.demo.UserService

@Theme("mytheme")
@VaadinUI(path = '/')
class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        VerticalLayout layout = new VerticalLayout()

        String homeLabel = Grails.i18n("default.home.label")
        Label label = new Label(homeLabel)
        layout.addComponent(label)

        // example of how to get Grails service and call a method to load data from database
        UserService userService = Grails.get(UserService)
        List<User> users = userService.findUsers()
        for (User user : users) {
            layout.addComponent(new Label(user.name))
        }

        setContent(layout)
    }
}
