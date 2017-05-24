package com.vaadinongrails

import org.gradle.api.Plugin
import org.gradle.api.Project

class VaadinGradlePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.tasks.create(name: 'vaadin-quickstart', type: QuickStartTask, group: 'vaadin')
        project.tasks.create(name: 'vaadin-spring-quickstart', type: SpringQuickStartTask, group: 'vaadin')

        project.tasks.create(name: 'vaadin-compile-widgetset', type: CompileWidgetsetTask, group: 'vaadin')
        project.tasks.create(name: 'vaadin-compile-sass', type: CompileSassTask, group: 'vaadin')

        project.getGradle().addProjectEvaluationListener(new DependencyListener())
    }

}