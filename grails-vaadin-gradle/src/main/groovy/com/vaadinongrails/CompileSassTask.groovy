package com.vaadinongrails

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction

import static java.io.File.separator

class CompileSassTask extends DefaultTask {

    CompileSassTask() {
        dependsOn('classes')

        description = "Compiles SASS and into CSS."
    }

    @TaskAction
    void run() {
        String path = "src${separator}main${separator}webapp${separator}VAADIN${separator}themes${separator}"
        File themesDir = new File(path)
        project.logger.info("SASS Compilation: " + themesDir.absolutePath)

        FileTree fileTree = project.fileTree(dir: themesDir, include: "**${separator}styles.scss")

        if (fileTree.isEmpty()) {
            throw new GradleException("SASS compilation failed because no SCSS files were found in: " + themesDir.absolutePath)
        }

        fileTree.each { File theme ->
            File dir = new File(theme.parent)
            println dir

            def compileProcess = ['java']
            compileProcess += ['-cp', project.sourceSets.main.compileClasspath.asPath]
            compileProcess += 'com.vaadin.sass.SassCompiler'
            compileProcess += [theme.canonicalPath, dir.canonicalPath + "${separator}styles.css"]

            Process process = compileProcess.execute()

            process.waitFor()
            project.logger.info("SASS Compiled")
        }
    }
}
