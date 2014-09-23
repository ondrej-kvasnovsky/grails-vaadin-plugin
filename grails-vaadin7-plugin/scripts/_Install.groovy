// Create VaadinConfig.groovy only if it does not exist
if (!(new File("${basedir}/grails-app/conf/VaadinConfig.groovy")).exists()) {
    ant.copy(file: "${pluginBasedir}/src/samples/_VaadinConfig.template",
            tofile: "${basedir}/grails-app/conf/VaadinConfig.groovy")
}
