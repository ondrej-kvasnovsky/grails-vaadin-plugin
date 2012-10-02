import org.slf4j.Logger
import org.slf4j.LoggerFactory
import grails.vaadin7.demo.User

class BootStrap {

    private static final transient Logger log = LoggerFactory.getLogger(this)

    def init = { servletContext ->
        User user1 = new User()
        user1.name = "Ondrej"
        user1.save(failOnError: true)

        User user2 = new User()
        user2.name = "Bara"
        user2.save(failOnError: true)
    }
    def destroy = {
    }
}
