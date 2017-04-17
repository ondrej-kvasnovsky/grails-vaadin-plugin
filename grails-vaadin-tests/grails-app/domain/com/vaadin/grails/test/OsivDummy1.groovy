package com.vaadin.grails.test

class OsivDummy1 {

    String name

    static constraints = {
    }

    static hasMany = [dummy: OsivDummy2]

    @Override
    public String toString() {
        return "OsivDummy1 ${dummy*.name}"
    }
}
