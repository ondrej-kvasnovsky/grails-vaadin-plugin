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

    vaadinVersion = "7.3.3"

    dependencies {
        compile "com.vaadin:vaadin-server:${vaadinVersion}"
        compile "com.vaadin:vaadin-client-compiled:${vaadinVersion}"
        compile "com.vaadin:vaadin-client:${vaadinVersion}"
        compile "com.vaadin:vaadin-client-compiler:${vaadinVersion}"
        compile "com.vaadin:vaadin-themes:${vaadinVersion}"
        compile "com.vaadin:vaadin-push:${vaadinVersion}"

        compile 'commons-dbcp:commons-dbcp:1.4'
    }

    plugins {
        build(':release:3.0.1', ':rest-client-builder:1.0.3') {
            export = false
        }
    }
}
