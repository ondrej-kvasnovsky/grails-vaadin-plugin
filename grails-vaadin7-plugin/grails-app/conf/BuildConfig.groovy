grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()
        mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.com/maven2/"
        mavenRepo "http://oss.sonatype.org/content/repositories/vaadin-snapshots"
        mavenRepo "http://maven.vaadin.com/vaadin-addons"
        mavenRepo "http://repo.springsource.org/release"

        grailsRepo "http://grails.org/plugins"
    }

    dependencies {
        compile 'com.vaadin:vaadin-server:7.0.1'
        compile 'com.vaadin:vaadin-client-compiled:7.0.1'
        compile 'com.vaadin:vaadin-client:7.0.1'
        compile 'com.vaadin:vaadin-themes:7.0.1'
    }

    plugins {
        build ":tomcat:$grailsVersion"
        build(':release:2.2.0', ':rest-client-builder:1.0.2') {
            export = false
        }
    }
}
