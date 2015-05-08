import com.vaadin.grails.VaadinGrailsPlugin

import java.util.jar.JarFile

description "Creates minimal required files to run Vaadin application in Grails", "grails vaadin-quickstart"

File pluginDir = new File(VaadinGrailsPlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath());

String urlMappings = "META-INF/templates/_UrlMappings.template"
String vaadinConfig = "META-INF/templates/_VaadinConfig.template"
String myUi = "META-INF/templates/_MyUI.template"

JarFile jarFile = new JarFile(pluginDir)

String vaadinConfigPath = baseDir.absolutePath + "/grails-app/conf/VaadinConfig.groovy"
File vaadinConfigFile = new File(vaadinConfigPath)
vaadinConfigFile.write(jarFile.getInputStream(jarFile.getJarEntry(vaadinConfig)).text)

String urlMappingsPath = baseDir.absolutePath + "/grails-app/controllers/UrlMappings.groovy"
File urlMappingsFile = new File(urlMappingsPath)
urlMappingsFile.write(jarFile.getInputStream(jarFile.getJarEntry(urlMappings)).text)


String myUiPackage = baseDir.absolutePath + "/src/main/groovy/app"
new File(myUiPackage).mkdir()

String myUiPath = myUiPackage + "/MyUI.groovy"
File myUiFile = new File(myUiPath)
myUiFile.write(jarFile.getInputStream(jarFile.getJarEntry(myUi)).text)
