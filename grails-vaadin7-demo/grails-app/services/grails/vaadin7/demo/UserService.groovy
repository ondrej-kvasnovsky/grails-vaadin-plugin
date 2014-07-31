package grails.vaadin7.demo

import grails.transaction.Transactional

@Transactional
class UserService {

    List<User> findUsers() {
        List<User> res = User.list()
        return res
    }
}
