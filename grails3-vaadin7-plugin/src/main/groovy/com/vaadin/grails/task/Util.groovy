package com.vaadin.grails.task

import org.gradle.api.Project
import org.gradle.api.file.FileCollection

class Util {

    static FileCollection getClientCompilerClassPath(Project project) {
        FileCollection collection = project.sourceSets.main.runtimeClasspath
        collection += project.sourceSets.main.compileClasspath

        getMainSourceSet(project).srcDirs.each {
            collection += project.files(it)
        }

        project.sourceSets.main.java.srcDirs.each { File dir ->
            collection += project.files(dir)
        }

        if (project.vaadin.gwt.gwtSdkFirstInClasspath) {
//            FileCollection gwtCompilerClasspath = project.configurations[DependencyListener.Configuration.CLIENT.caption];
//            collection = gwtCompilerClasspath + collection.minus(gwtCompilerClasspath);
        }

        collection
    }

    def static logProcess(final Project project, final Process process, final String filename, Closure monitor = {}) {
        if (project.vaadin.plugin.logToConsole) {
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
            File logDir = project.file('build/logs/')
            logDir.mkdirs()

            final File logFile = new File(logDir.canonicalPath + '/' + filename)
            Thread.start 'Info logger', {
                logFile.withWriterAppend { out ->
                    try {
                        def errorOccurred = false
                        process.inputStream.eachLine { output ->
                            monitor.call(output)
                            if (output.contains("[WARN]")) {
                                out.println "[WARN] " + output.replaceAll("\\[WARN\\]", '').trim()
                            } else if (output.contains('[ERROR]')) {
                                errorOccurred = true
                                out.println "[ERROR] " + output.replaceAll("\\[ERROR\\]", '').trim()
                            } else {
                                out.println "[INFO] " + output.trim()
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
            Thread.start 'Error logger', {
                logFile.withWriterAppend { out ->
                    try {
                        process.errorStream.eachLine { output ->
                            monitor.call(output)
                            project.logger.error(output.replaceAll("\\[ERROR\\]", '').trim())
                            out.println "[ERROR] " + output.replaceAll("\\[ERROR\\]", '').trim()
                            out.flush()
                        }
                    } catch (IOException e) {
                        // Stream might be closed
                    }
                }
            }
        }
    }
}
