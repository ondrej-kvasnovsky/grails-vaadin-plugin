package com.vaadinongrails

import grails.util.Environment
import org.gradle.api.Plugin
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile

class VaadinGradlePlugin implements Plugin<Project> {

    void apply(Project project) {

        project.task('vaadin-quickstart') << {
            println "Vaadin QuickStart task"
//            println project.sourceSets.main
            File pluginJar = new File(VaadinGradlePlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            JarFile jar = new JarFile(pluginJar);
            Enumeration enumEntries = jar.entries();
            while (enumEntries.hasMoreElements()) {
                JarEntry file = (JarEntry) enumEntries.nextElement();
                if (file.name.startsWith("templates${File.separator}") && !file.name.startsWith("templates${File.separator}spring${File.separator}") && !file.isDirectory()) {
                    println file.name
                }
            }

            println "File: "
            println pluginJar.absolutePath

            File file = new File("grails-app${File.separator}conf${File.separator}VaadinConfig.groovy")
            Map vaadinConfig = new ConfigSlurper(Environment.current.name).parse(file.text)

            println vaadinConfig
        }

        project.task('vaadin-spring-quickstart') << {
            println "Vaadin QuickStart task"
//            println project.sourceSets.main
            File pluginJar = new File(VaadinGradlePlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            JarFile jar = new JarFile(pluginJar);
            Enumeration enumEntries = jar.entries();
            while (enumEntries.hasMoreElements()) {
                JarEntry file = (JarEntry) enumEntries.nextElement();
                if (file.name.startsWith("templates${File.separator}") && file.name.startsWith("templates${File.separator}spring${File.separator}") && !file.isDirectory()) {
                    println file.name
                }
            }

            println "File: "
            println pluginJar.absolutePath

            File file = new File("grails-app${File.separator}conf${File.separator}VaadinConfig.groovy")
            Map vaadinConfig = new ConfigSlurper(Environment.current.name).parse(file.text)

            println vaadinConfig
        }

        project.tasks.create(name: 'vaadin-compile-widgetset', type: CompileWidgetsetTask, group: 'Vaadin')
    }

//    static FileCollection getCompileClassPathOrJar(Project project) {
//        def vaadin = project.vaadin as VaadinPluginExtension
//        FileCollection classpath
//        if(vaadin.plugin.useClassPathJar) {
//            // Add dependencies using the classpath jar
//            BuildClassPathJar pathJarTask = project.getTasksByName(BuildClassPathJar.NAME, true).first()
//            classpath = project.files(pathJarTask.archivePath)
//        } else {
//            classpath = getCompileClassPath(project)
//        }
//        classpath
//    }


}