package vaadin7

import com.vaadin.ui.Button
import com.vaadin.ui.Notification
import com.vaadin.ui.UI
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.Label
import grails.vaadin7.demo.UserService
import grails.vaadin7.demo.User
import com.vaadin.grails.Grails
import com.vaadin.ui.VerticalLayout

/**
 *
 *
 * @author Ondrej Kvasnovsky
 */
class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        List<User> users = Grails.get(UserService).getListOfUsers()
        VerticalLayout layout = new VerticalLayout()
        layout.addComponent(new Label(Grails.i18n("aloha")))
        layout.addComponent(new Label(Grails.i18n("aloha-which-doesnt-exist")))
        for (User user : users) {
            Button button = new Button(user.name)
            button.addClickListener(new Button.ClickListener() {
                @Override
                void buttonClick(Button.ClickEvent event) {
                    Notification.show("Interesting...")
                }
            })
            layout.addComponent(button)
        }
        setContent(layout)
    }
}
