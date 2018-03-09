package com.vaadinongrails

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencyResolveDetails

/**
 * Reused from https://github.com/johndevs/gradle-vaadin-plugin
 */
class DependencyListener implements ProjectEvaluationListener {

    static enum VaadinConfiguration {
        SERVER('vaadin', 'Vaadin server side libraries'),
        CLIENT('vaadin-client', 'Client side libraries used by Vaadin to compile the widgetset'),

        def String caption
        def String description;

        public VaadinConfiguration(String caption, String description) {
            this.caption = caption
            this.description = description
        }
    }

    void beforeEvaluate(Project project) {

        if (project.plugins.findPlugin('eclipse') && !project.plugins.findPlugin('eclipse-wtp')) {
            project.getLogger().warn("You are using the eclipse plugin which does not support all " +
                    "features of the Vaadin plugin. Please use the eclipse-wtp plugin instead.")
        }
    }

    void afterEvaluate(Project project, ProjectState state) {

        String version = "8.3.1"

        createVaadin7Configuration(project, version)

        project.configurations.all { config ->
            configureResolutionStrategy(project, config)
        }
    }

    Configuration createConfiguration(Project project,
                                      VaadinConfiguration conf,
                                      List<String> dependencies,
                                      Iterable<Configuration> extendsFrom = null) {

        Configuration configuration

        if (extendsFrom) {
            configuration = project.configurations.maybeCreate(conf.caption).setExtendsFrom(extendsFrom as Set)
        } else {
            configuration = project.configurations.maybeCreate(conf.caption)
        }

        configuration.description = conf.description

        dependencies.each { dependency ->
            project.dependencies.add(conf.caption, dependency)
        }

        configuration
    }

    void configureResolutionStrategy(Project project, Configuration config) {
        final List whitelist = [
                'com.vaadin:vaadin-client',
                'com.vaadin:vaadin-client-compiled',
                'com.vaadin:vaadin-client-compiler',
                'com.vaadin:vaadin-server',
                'com.vaadin:vaadin-shared',
                'com.vaadin:vaadin-themes',
                'com.vaadin:vaadin-push'
        ]

        config.resolutionStrategy.eachDependency(new Action<DependencyResolveDetails>() {
            @Override
            void execute(DependencyResolveDetails details) {
                def dependency = details.requested
                String group = dependency.group
                String name = dependency.name
                if ("$group:$name".toString() in whitelist) {
                    details.useVersion "8.3.1"
                }

                if (config.name == VaadinConfiguration.CLIENT.caption) {
                    // we need to get rid of all validation-api of version 1.1.0 because it would cause runtime errors
                    if (group == 'javax.validation' && name == 'validation-api') {
                        // GWT only supports this version, do not upgrade it
                        details.useVersion '1.0.0.GA'
                    }
                }
            }
        })
    }

    void createServerConfiguration(Project project) {
        def conf = createConfiguration(project, VaadinConfiguration.SERVER, [], [project.configurations.compile])

        def sources = project.sourceSets.main
        sources.compileClasspath = sources.compileClasspath.plus(conf)

        def testSources = project.sourceSets.test
        testSources.compileClasspath = testSources.compileClasspath.plus(conf)
        testSources.runtimeClasspath = testSources.runtimeClasspath.plus(conf)
    }

    void createVaadin7Configuration(Project project, String version) {

        createServerConfiguration(project)

        createClientConfiguration(project)

        def serverConf = VaadinConfiguration.SERVER
        def clientConf = VaadinConfiguration.CLIENT
        def dependencies = project.dependencies

        dependencies.add(serverConf.caption, "com.vaadin:vaadin-client-compiled:${version}")
        dependencies.add(clientConf.caption, "com.vaadin:vaadin-client-compiler:${version}", {
            exclude([group: 'org.mortbay.jetty'])  // pre 7.2.2
            exclude([group: 'org.eclipse.jetty'])
        })

        dependencies.add(clientConf.caption, "com.vaadin:vaadin-client:${version}")
        dependencies.add(clientConf.caption, "javax.validation:validation-api:1.0.0.GA")

        dependencies.add(serverConf.caption, "com.vaadin:vaadin-server:${version}")

        dependencies.add(serverConf.caption, "com.vaadin:vaadin-themes:${version}")
    }

    void createClientConfiguration(Project project) {
        def conf = createConfiguration(project, VaadinConfiguration.CLIENT, [], [project.configurations.compile])

        def sources = project.sourceSets.main
        sources.compileClasspath = sources.compileClasspath.plus(conf)

        def testSources = project.sourceSets.test
        testSources.compileClasspath = testSources.compileClasspath.plus(conf)
        testSources.runtimeClasspath = testSources.runtimeClasspath.plus(conf)
    }
}
