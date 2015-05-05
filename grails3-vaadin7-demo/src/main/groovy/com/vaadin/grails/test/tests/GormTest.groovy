package com.vaadin.grails.test.tests

import com.vaadin.grails.Grails
import com.vaadin.grails.test.User
import com.vaadin.grails.test.UserService
import com.vaadin.grails.test.utils.PluginTest
import com.vaadin.grails.test.utils.PluginTestSuite

@PluginTestSuite
class GormTest {

    @PluginTest
    public void 'Returns users from database'() {
        List<User> users = Grails.get(UserService).findUsers()
        assert users.size() > 0
    }
}
