grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {

    inherits("global") {
    }

    log "warn"

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
    }

    dependencies {
        compile 'com.vaadin:vaadin-server:7.1.9'
        compile 'com.vaadin:vaadin-client-compiled:7.1.9'
        compile 'com.vaadin:vaadin-client:7.1.9'
        compile 'com.vaadin:vaadin-themes:7.1.9'
    }

    plugins {
        build(':release:3.0.0', ':rest-client-builder:1.0.3') {
            export = false
        }
    }
}
