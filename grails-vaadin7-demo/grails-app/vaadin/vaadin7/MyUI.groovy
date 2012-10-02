package vaadin7

import com.vaadin.ui.UI
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.Label
import grails.vaadin7.demo.UserService
import grails.vaadin7.demo.User
import com.vaadin.grails.Grails

/**
 *
 *
 * @author Ondrej Kvasnovsky
 */
class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        List<User> users = Grails.get(UserService).getListOfUsers()
        addComponent(new Label(Grails.i18n("aloha")))
        addComponent(new Label(Grails.i18n("aloha-which-doesnt-exist")))
        for (User user : users) {
            addComponent(new Label(user.name))
        }
    }
}
