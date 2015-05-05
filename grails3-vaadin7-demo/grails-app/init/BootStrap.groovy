import com.vaadin.grails.test.OsivDummy1
import com.vaadin.grails.test.OsivDummy2
import com.vaadin.grails.test.User

import javax.servlet.ServletContext

class BootStrap {

    def init = { ServletContext servletContext ->
        User user1 = new User()
        user1.name = "John"
        user1.save()

        OsivDummy1 osivDummy1 = new OsivDummy1(name: "X")
        1..100.each {
            osivDummy1.addToDummy(new OsivDummy2(name: it.toString()))
        }
        osivDummy1.save()
    }
    def destroy = {
    }
}
