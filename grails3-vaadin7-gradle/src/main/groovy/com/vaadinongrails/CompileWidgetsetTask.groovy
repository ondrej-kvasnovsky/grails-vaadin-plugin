package com.vaadinongrails

import grails.util.Environment
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskAction

import static java.io.File.separator

/**
 * Contains some code which is cherry picked from https://github.com/johndevs/gradle-vaadin-plugin
 */
class CompileWidgetsetTask extends DefaultTask {

    CompileWidgetsetTask() {

        dependsOn('classes')

        description = "Compiles Vaadin Addons and components into Javascript."

        project.afterEvaluate {
            inputs.files(project.configurations.compile)

            project.sourceSets.main.java.srcDirs.each {
                inputs.files(project.fileTree(it.absolutePath).include('**/*/client/**/*.java'))
                inputs.files(project.fileTree(it.absolutePath).include('**/*/shared/**/*.java'))
                inputs.files(project.fileTree(it.absolutePath).include('**/*/public/**/*.*'))
                inputs.files(project.fileTree(it.absolutePath).include('**/*/*.gwt.xml'))
            }

            project.sourceSets.main.resources.srcDirs.each {
                inputs.files(project.fileTree(it.absolutePath).include('**/*/public/**/*.*'))
                inputs.files(project.fileTree(it.absolutePath).include('**/*/*.gwt.xml'))
            }
        }
    }

    @TaskAction
    public void run() {

        File file = new File("grails-app${separator}conf${separator}VaadinConfig.groovy")
        Map vaadinConfig = new ConfigSlurper(Environment.current.name).parse(file.text)

        String widgetSetName = vaadinConfig.vaadin.widgetset
        if (widgetSetName) {
            String widgetSetPath = widgetSetName.replaceAll("\\.", separator)
            File widgetSet = new File("src${separator}main${separator}resources${separator}${widgetSetPath}.gwt.xml")
            if (widgetSet.exists()) {
                logger.info("Found: " + widgetSet.absolutePath)
                FileCollection classpath = getClientCompilerClassPath(project)
                logger.info("Path to Classpath: " + classpath.asPath)
                classpath.files.forEach {
                    logger.info("Classpath: " + it)
                }

                def widgetsetCompileProcess = ['java']

                widgetsetCompileProcess += ['-cp', classpath.asPath]

                widgetsetCompileProcess += 'com.google.gwt.dev.Compiler'

                widgetsetCompileProcess += ['-style', "OBF"]
                widgetsetCompileProcess += ['-optimize', 0]
                widgetsetCompileProcess += ['-war', "src/main/webapp/VAADIN/widgetsets"]
                widgetsetCompileProcess += ['-logLevel', "INFO"]
                widgetsetCompileProcess += ['-localWorkers', Runtime.getRuntime().availableProcessors() - 1]

                // widgetsetCompileProcess += '-draftCompile'
                widgetsetCompileProcess += '-strict'

                widgetsetCompileProcess += vaadinConfig.vaadin.widgetset

                Process process = widgetsetCompileProcess.execute()
                boolean failed = false
                logProcess(project, process, 'widgetset-compile.log', { String output ->
                    if (output.trim().startsWith('[ERROR]')) {
                        failed = true
                    }
                })

                process.waitFor()

                if (failed) {
                    throw new GradleException('Widgetset failed to compile. See error log above.')
                }
            } else {
                throw new GradleException("Widgetset file not found: " + widgetSet.absolutePath)
            }

        }
    }

    static void logProcess(final Project project, final Process process, final String filename, Closure monitor = {}) {
        if (true) {
            Thread.start 'Info logger', {
                try {
                    def errorOccurred = false
                    process.inputStream.eachLine { output ->
                        monitor.call(output)
                        if (output.contains("[WARN]")) {
                            project.logger.warn(output.replaceAll("\\[WARN\\]", '').trim())
                        } else if (output.contains('[ERROR]')) {
                            errorOccurred = true
                        } else {
                            project.logger.info(output.trim())
                        }
                        if (errorOccurred) {
                            // An error has occurred, dump everything to console
                            project.logger.error(output.replaceAll("\\[ERROR\\]", '').trim())
                        }
                    }
                } catch (IOException e) {
                    // Stream might be closed
                }
            }

            Thread.start 'Error logger', {
                try {
                    process.errorStream.eachLine { String output ->
                        monitor.call(output)
                        project.logger.error(output.replaceAll("\\[ERROR\\]", '').trim())
                    }
                } catch (IOException e) {
                    // Stream might be closed
                }
            }
        } else {
            File logDir = new File(project.buildDir, 'logs')
            logDir.mkdirs()

            final File logFile = new File(logDir, filename)
            Thread.start 'Info logger', {
                logFile.withWriterAppend { out ->
                    try {
                        def errorOccurred = false
                        process.inputStream.eachLine { output ->
                            monitor.call(output)
                            if (output.contains("[WARN]")) {
                                out.logger "[WARN] " + output.replaceAll("\\[WARN\\]", '').trim()
                            } else if (output.contains('[ERROR]')) {
                                errorOccurred = true
                                out.logger "[ERROR] " + output.replaceAll("\\[ERROR\\]", '').trim()
                            } else {
                                out.logger "[INFO] " + output.trim()
                            }
                            out.flush()
                            if (errorOccurred) {
                                // An error has occurred, dump everything to console
                                project.logger.error(output.replaceAll("\\[ERROR\\]", '').trim())
                            }
                        }
                    } catch (IOException e) {
                        // Stream might be closed
                    }
                }
            }
        }
    }

    static FileCollection getClientCompilerClassPath(Project project) {
        FileCollection collection = project.sourceSets.main.runtimeClasspath
        collection += project.sourceSets.main.compileClasspath

        project.sourceSets.main.groovy.srcDirs.each {
            collection += project.files(it)
        }

        project.sourceSets.main.java.srcDirs.each { File dir ->
            collection += project.files(dir)
        }

        moveGwtSdkFirstInClasspath(project, collection)

        collection
    }

    static FileCollection moveGwtSdkFirstInClasspath(Project project, FileCollection collection) {
        FileCollection gwtCompilerClasspath = project.configurations[DependencyListener.VaadinConfiguration.CLIENT.caption];
        return gwtCompilerClasspath + collection.minus(gwtCompilerClasspath);
    }

}
