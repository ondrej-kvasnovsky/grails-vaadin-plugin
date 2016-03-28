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
                if (file.name.startsWith("templates/") && !file.name.startsWith("templates/spring/") && !file.isDirectory()) {
                    println file.name
                }
            }


            println "File: "
            println pluginJar.absolutePath

            File file = new File("grails-app/conf/VaadinConfig.groovy")
            Map vaadinConfig = new ConfigSlurper(Environment.current.name).parse(file.text)

            println vaadinConfig
        }

        project.task('vaadin-compile-widgetset') << {

        }
    }
}