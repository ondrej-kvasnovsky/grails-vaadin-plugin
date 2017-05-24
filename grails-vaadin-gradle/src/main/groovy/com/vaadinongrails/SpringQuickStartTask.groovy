package com.vaadinongrails

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.util.jar.JarEntry
import java.util.jar.JarFile

public class SpringQuickStartTask extends DefaultTask {

    private static final String s = File.separator

    @TaskAction
    void run() {
        println "Vaadin Spring QuickStart task"
        File pluginJar = new File(VaadinGradlePlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        JarFile jar = new JarFile(pluginJar);
        Enumeration enumEntries = jar.entries();

        String path = project.getProjectDir().absolutePath
        println "Project path: " + path

        while (enumEntries.hasMoreElements()) {
            JarEntry from = (JarEntry) enumEntries.nextElement();
            if (from.name.startsWith("templates${s}spring${s}") && !from.isDirectory()) {

                String fileName = from.name.replace("templates${s}spring${s}", '')
                String destPath
                if ("VaadinConfig.groovy" == fileName) {
                    destPath = s + "grails-app" + s + "conf" + s
                } else if ("MyUI.groovy" == fileName || "ItemView.groovy" == fileName) {
                    destPath = s + "src" + s + "main" + s + "groovy" + s + "com" + s + "app" + s
                } else if ("UrlMappings.groovy" == fileName) {
                    destPath = s + "grails-app" + s + "controllers" + s + "com" + s + "app" + s
                }
                if (destPath != null) {
                    File to = new File(path + destPath + fileName)
                    InputStream stream = jar.getInputStream(from)
                    FileUtils.copyInputStreamToFile(stream, to)
                    println from.name + " copied to " + to
                }
            }
        }
    }
}
