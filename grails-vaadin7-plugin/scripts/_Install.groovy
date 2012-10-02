//
// This script is executed by Grails after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder

ant.copy(file:"${pluginBasedir}/src/samples/_VaadinConfig.template",
        tofile:"${basedir}/grails-app/conf/VaadinConfig.groovy")

ant.mkdir(dir:"${basedir}/grails-app/vaadin")

ant.mkdir(dir:"${basedir}/grails-app/vaadin/app")
ant.copy(file:"${pluginBasedir}/src/samples/_MyUI.template",
        tofile:"${basedir}/grails-app/vaadin/app/MyUI.groovy")

ant.delete(file:"${pluginBasedir}/grails-app/conf/UrlMappings.groovy")

ant.copy(file:"${pluginBasedir}/src/samples/_UrlMappings.template",
        tofile:"${basedir}/grails-app/conf/UrlMappings.groovy")
