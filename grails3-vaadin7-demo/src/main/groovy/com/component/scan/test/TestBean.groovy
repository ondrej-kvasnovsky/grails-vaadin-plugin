package com.component.scan.test

import org.springframework.stereotype.Component

@Component
// a bean in other package than com.vaadin.grails to test 'packages = []' configuration
class TestBean {

    String what = 'WHAT?'
}
