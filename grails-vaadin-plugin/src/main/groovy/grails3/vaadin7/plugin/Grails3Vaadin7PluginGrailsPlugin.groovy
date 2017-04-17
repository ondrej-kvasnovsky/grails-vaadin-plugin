package grails3.vaadin7.plugin

import grails.plugins.Plugin

class Grails3Vaadin7PluginGrailsPlugin extends Plugin {
    def grailsVersion = "3.0.0 > *"
    def title = "Grails3 Vaadin7 Plugin"
    def author = "Ondrej Kvasnovsky"
    def authorEmail = "ondrej.kvasnovsky@gmail.com"
    def description = 'Integrated Vaadin 7 in Grails'
    def profiles = ['web']
    def documentation = "http://vaadinongrails.com"
    def license = "APACHE"
    def organization = [name: "Ondrej Kvasnovsky", url: "http://vaadinongrails.com"]
    def issueManagement = [url: 'https://github.com/ondrej-kvasnovsky/grails-vaadin-plugin/issues']
    def scm = [url: 'https://github.com/ondrej-kvasnovsky/grails-vaadin-plugin']
}
