//
// This script is executed by Grails during application upgrade ('grails upgrade'
// command). This script is a Gant script so you can use all special variables
// provided by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder

if (!(new File("${basedir}/grails-app/conf/VaadinConfig.groovy")).exists()) {
    ant.copy(file: "${pluginBasedir}/src/samples/_VaadinConfig.template",
            tofile: "${basedir}/grails-app/conf/VaadinConfig.groovy")
}
