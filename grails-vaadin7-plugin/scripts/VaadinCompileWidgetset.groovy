includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsClasspath")
includeTargets << grailsScript("_GrailsRun")

target(widgetset_init: "init") {
    vaadinConfig = new ConfigSlurper(grails.util.Environment.current.name).parse(new File("${basedir}/grails-app/conf/VaadinConfig.groovy").text)
    ant.property(name: "widgetset", value: vaadinConfig.vaadin.widgetset)
    ant.property(name: "workers", value: vaadinConfig.vaadin.compile?.widgetset?.workers?:"4")
    ant.property(name: "forkJava", value: vaadinConfig.vaadin.compile?.forkJava ? vaadinConfig.vaadin.compile?.forkJava : true)
    ant.property(name: "usePathJar", value: vaadinConfig.vaadin.compile?.usePathJar ? vaadinConfig.vaadin.compile?.usePathJar : false)
    ant.property(name: "classPathRegExp", value: vaadinConfig.vaadin.compile?.classPathRegExp?: "(?ismx)([^;]*(vaadin|gwt-dev|ant|sac)[^;]*);")
    ant.property(name: "antMaxMemory", value: vaadinConfig.vaadin.compile?.antMaxMemory?: "512m")
    ant.property(name: "gwtCompilerXss", value: vaadinConfig.vaadin.compile?.gwtCompilerXss?: "1024k")
    ant.property(name: "widgetset-path", value: "")
    ant.property(name: "client-side-destination", value: "web-app/VAADIN/widgetsets")
    ant.property(name: "generate.widgetset", value: "1")
}

USAGE = """
vaadin-compile-widgetset
Compiles the widgetset configured in grails-app/conf/VaadinConfig.groovy
"""
target(compile_widgetset: "Compiles the widgetset configured in grails-app/conf/VaadinConfig.groovy") {
    depends(classpath, compile, widgetset_init)
    ant.echo message: """Compiling ${ant.project.properties.'widgetset'} into ${ant.project.properties."client-side-destination"} directory..."""
    if (ant.project.properties.'usePathJar'.equals("true")) {
        ant.property(name: "gcp", refid: "grails.compile.classpath")
        ant.jar(destfile: "$projectTargetDir/path.jar") {
            manifest {
                attribute(name: "Class-Path", value: ant.project.properties.gcp)
            }
        }
        ant.path (id: 'gwt.compile') {
            pathelement location: "$projectTargetDir/path.jar"
            def matcher =  (ant.project.properties.gcp =~ ant.project.properties.'classPathRegExp' )
            for (int i = 0; i < matcher.size(); i++) {
                pathelement location: matcher[i][1]
            }
        }
    }
    ant.java(classname: "com.google.gwt.dev.Compiler", maxmemory: ant.project.properties.'antMaxMemory', failonerror: true,
            fork: ant.project.properties.'forkJava',
            classpathref: ant.project.properties.'usePathJar'.equals("true") ? "gwt.compile" : "grails.compile.classpath") {
        ant.classpath {
            if (ant.project.properties.'usePathJar'.equals("true"))
                pathelement location: "$projectTargetDir/path.jar"
            pathelement location: "${basedir}/src/java"
            pathelement location: "${basedir}/target/classes"
        }
        arg(value: "-localWorkers")
        arg(value: "${ant.project.properties.'workers'}")
        arg(value: "-war")
        arg(value: ant.project.properties.'client-side-destination')
        arg(value: ant.project.properties.'widgetset')
        jvmarg(value: "-Xss${ant.project.properties.'gwtCompilerXss'}")
        jvmarg(value: "-Djava.awt.headless=true")
    }
}

setDefaultTarget(compile_widgetset)
