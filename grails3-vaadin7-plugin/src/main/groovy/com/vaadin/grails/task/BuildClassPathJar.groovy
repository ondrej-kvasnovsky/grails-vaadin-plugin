package com.vaadin.grails.task

import org.gradle.api.tasks.bundling.Jar

class BuildClassPathJar extends Jar {

    public static final String NAME = 'vaadinClassPathJar'

    BuildClassPathJar() {
        description = 'Creates a Jar with the project classpath'
        classifier = 'classpath'
        dependsOn 'classes'

        onlyIf {
            project.vaadin.plugin.useClassPathJar
        }

        project.afterEvaluate{
            def files = project.sourceSets.main.compileClasspath.filter { File file ->
                file.isFile() && file.name.endsWith('.jar')
            }

            inputs.files(files)

            manifest.attributes('Class-Path': files.collect { File file ->
                file.toURI().toString()
            }.join(' '))
        }
    }
}