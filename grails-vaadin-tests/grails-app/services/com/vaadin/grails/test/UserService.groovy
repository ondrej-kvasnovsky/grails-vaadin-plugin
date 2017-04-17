package com.vaadin.grails.test

class UserService {

    public List<User> findUsers() {
        return User.findAll()
    }
}
