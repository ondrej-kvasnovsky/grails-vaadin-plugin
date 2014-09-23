import grails.util.BuildSettingsHolder

includeTargets << grailsScript("_GrailsInit")

target(vaadinQuickstart: "Generates MyUI class and removed UrlMapping to easier project startup.") {

    // Get Vaadin plugin base dir to look for templates
    String pluginBasedir
    BuildSettingsHolder.settings.pluginDirectories.each { File dir ->
        String path = dir.absolutePath
        if (path.split(File.separator).last().contains('vaadin')) {
            pluginBasedir = path
        }
    }

    // Create MyUI sample class
    ant.mkdir(dir: "${basedir}/src/groovy/app")
    ant.copy(file: "${pluginBasedir}/src/samples/_MyUI.template",
            tofile: "${basedir}/src/groovy/app/MyUI.groovy")

    // Replace existing UrlMappings with empty one
    ant.delete(file: "${basedir}/grails-app/conf/UrlMappings.groovy")
    ant.copy(file: "${pluginBasedir}/src/samples/_UrlMappings.template",
            tofile: "${basedir}/grails-app/conf/UrlMappings.groovy")
}

setDefaultTarget(vaadinQuickstart)
