import grails.vaadin7.demo.User

class BootStrap {

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
