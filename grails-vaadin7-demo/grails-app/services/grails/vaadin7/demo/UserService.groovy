package grails.vaadin7.demo

class UserService {

    List<User> getListOfUsers() {
        List<User> res = User.list()
        return res
    }
}
