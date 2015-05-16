import grails.util.BuildSettingsHolder

includeTargets << grailsScript("_GrailsInit")

target(vaadinQuickstart: "Generates MyUI class and removed UrlMapping to easier project startup.") {
    // Create MyUI sample class
    ant.mkdir(dir: "${basedir}/src/groovy/app")
    ant.copy(file: "${vaadinPluginDir}/src/samples/_MyUI.template",
            tofile: "${basedir}/src/groovy/app/MyUI.groovy")

    // Replace existing UrlMappings with empty one
    ant.delete(file: "${basedir}/grails-app/conf/UrlMappings.groovy")
    ant.copy(file: "${vaadinPluginDir}/src/samples/_UrlMappings.template",
            tofile: "${basedir}/grails-app/conf/UrlMappings.groovy")
}

setDefaultTarget(vaadinQuickstart)
