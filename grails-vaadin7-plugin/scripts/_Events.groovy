import org.codehaus.gant.GantState

eventWarStart = { warName ->
    GantState.verbosity = GantState.VERBOSE
    ant.logger.setMessageOutputLevel(GantState.verbosity)

    ConfigObject config = classLoader.loadClass('com.vaadin.grails.VaadinConfiguration').newInstance(classLoader).config
    ant.echo("Vaadin plugin config: $config")

    if (config?.productionMode) {
        // compile SCSS when in production mode... aka Vaadin does no on-the-fly-compilation
        ant.echo('SASS compilation: Starting')
        ant.echo("SASS compilation: Themes for compilation: ${config?.themes}")
        if (config?.themes instanceof List) {
            List<String> themes = config?.themes
            for (String theme : themes) {
                try {
                    String path = "${basedir}/web-app/VAADIN/themes/$theme/"
                    File sassSource = new File("${path}styles.scss")
                    File sassDestination = new File("${path}styles.css")
                    ant.echo("SASS compilation: Input file: $sassSource")
                    ant.echo("SASS compilation: Output file: $sassDestination")
                    if (!sassSource.exists()) {
                        ant.echo("Source file for SASS compilation do not exist: $sassSource")
                    } else {
                        ant.property(name: "gcp", refid: "grails.compile.classpath")
                        ant.java(classpath: ant.project.properties.gcp, classname: 'com.vaadin.sass.SassCompiler', fork: false) {
                            arg(value: sassSource.absolutePath)
                            arg(value: sassDestination.absolutePath)
                        }
                        ant.echo('SASS compilation: Success')
                    }
                } catch (Exception e) {
                    ant.echo("SASS compilation: Failed")
                    e.printStackTrace()
                }
            }
        }
    }

    GantState.verbosity = GantState.WARNINGS_AND_ERRORS
    ant.logger.setMessageOutputLevel(GantState.verbosity)
}

eventCreateWarStart = { name, stagingDir ->
    GantState.verbosity = GantState.VERBOSE
    ant.logger.setMessageOutputLevel(GantState.verbosity)

    def config = classLoader.loadClass('com.vaadin.grails.VaadinConfiguration').newInstance(classLoader).config
    ant.echo("Vaadin plugin config: $config")

    // Allow override's in Config.groovy
    boolean removeClientJar = config?.removeClientJar ?: true

    // Remove the vaadin-client jar from the web archive to reduce the size, this jar is only needed for client dev and
    // not in the war carchive, the files size is 15 mb
    if (removeClientJar) {
        ant.delete(dir: "${stagingDir}/WEB-INF/lib/", includes: "vaadin-client-7.*.*.jar", verbose: true)
    }

    // remove the client compiler in any case
    ant.delete(dir: "${stagingDir}/WEB-INF/lib/", includes: "vaadin-client-compiler-7.*.*.jar", verbose: true)

    // remove the theme compiler in any case
    ant.delete(dir: "${stagingDir}/WEB-INF/lib/", includes: "vaadin-theme-compiler-7.*.*.jar", verbose: true)

    // remove gwt-unitCache (by-product of widgetset compilation)
    ant.delete(dir: "${stagingDir}/VAADIN/gwt-unitCache", verbose: true)

    GantState.verbosity = GantState.WARNINGS_AND_ERRORS
    ant.logger.setMessageOutputLevel(GantState.verbosity)
}
