//description "Compiles the widgetset configured in grails-app/conf/VaadinConfig.groovy", "grails vaadin-compile-widgetset"
//
//log "Starting compilation of WidgetSet..."
Map vaadinConfig = new ConfigSlurper(grails.util.Environment.current.name).parse(new File("${baseDir}/grails-app/conf/VaadinConfig.groovy").text)

ant.property(name: "widgetset", value: vaadinConfig.vaadin.widgetset)
ant.property(name: "workers", value: vaadinConfig.vaadin.compile?.widgetset?.workers ?: "4")
ant.property(name: "forkJava", value: vaadinConfig.vaadin.compile?.forkJava ? vaadinConfig.vaadin.compile?.forkJava : true)
ant.property(name: "usePathJar", value: vaadinConfig.vaadin.compile?.usePathJar ? vaadinConfig.vaadin.compile?.usePathJar : false)
ant.property(name: "classPathRegExp", value: vaadinConfig.vaadin.compile?.classPathRegExp ?: "(?ismx)([^;]*(vaadin|gwt-dev|ant|sac)[^;]*);")
ant.property(name: "antMaxMemory", value: vaadinConfig.vaadin.compile?.antMaxMemory ?: "512m")
ant.property(name: "gwtCompilerXss", value: vaadinConfig.vaadin.compile?.gwtCompilerXss ?: "1024k")
ant.property(name: "widgetset-path", value: "")
ant.property(name: "client-side-destination", value: "web-app/VAADIN/widgetsets")
ant.property(name: "generate.widgetset", value: "1")

ant.echo message: """Compiling ${ant.project.properties.'widgetset'} into ${
    ant.project.properties."client-side-destination"
} directory..."""
if (ant.project.properties.'usePathJar'.equals("true")) {
    ant.property(name: "gcp", refid: "grails.compile.classpath")
    ant.jar(destfile: "$projectTargetDir/path.jar") {
        manifest {
            attribute(name: "Class-Path", value: ant.project.properties.gcp)
        }
    }
    ant.path(id: 'gwt.compile') {
        pathelement location: "$projectTargetDir/path.jar"
        def matcher = (ant.project.properties.gcp =~ ant.project.properties.'classPathRegExp')
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
        pathelement location: "${baseDir}/src/java"
        pathelement location: "${baseDir}/target/classes"
    }
    arg(value: "-localWorkers")
    arg(value: "${ant.project.properties.'workers'}")
    arg(value: "-war")
    arg(value: ant.project.properties.'client-side-destination')
    arg(value: ant.project.properties.'widgetset')
    jvmarg(value: "-Xss${ant.project.properties.'gwtCompilerXss'}")
    jvmarg(value: "-Djava.awt.headless=true")
}

//println classesDir
//String[] args = [
//        "-localWorkers",
//        "4",
//        "-war",
//        "webapp/VAADIN/widgetsets",
//        "app.widgetset"
//]
//println com.google.gwt.dev.Compiler.main(args)
//ant.property(name: "widgetset", value: config.widgetset)
//ant.property(name: "workers", value: config.compile?.widgetset?.workers ?: "4")
//ant.property(name: "forkJava", value: true)
//ant.property(name: "usePathJar", value: config.compile?.usePathJar ? vaadinConfig.vaadin.compile?.usePathJar : false)
//ant.property(name: "classPathRegExp", value: config.compile?.classPathRegExp ?: "(?ismx)([^;]*(vaadin|gwt-dev|ant|sac)[^;]*);")
//ant.property(name: "antMaxMemory", value: "512m")
//ant.property(name: "gwtCompilerXss", value: config.compile?.gwtCompilerXss ?: "1024k")
//ant.property(name: "widgetset-path", value: "")

//ant.property(name: "client-side-destination", value: "webapp/VAADIN/widgetsets")
//ant.property(name: "generate.widgetset", value: "1")

//ant.path(id: "grails.compile.classpath", compileClasspath)
//println config.widgetset
////
//ant.java(classname: "com.google.gwt.dev.Compiler", maxmemory: ant.project.properties.'antMaxMemory', failonerror: true,
//        fork: ant.project.properties.'forkJava'
////        ,
////        classpathref: "grails.compile.classpath"
//) {
//    ant.classpath {
//        pathelement location: "${baseDir}/build/resources"
//        pathelement location: "${baseDir}/build/classes"
//    }
//    arg(value: "-localWorkers")
//    arg(value: "4")
//    arg(value: "-war")
//    arg(value: "webapp/VAADIN/widgetsets")
//    arg(value: config.widgetset)
//    arg(value: "-logLevel")
//    arg(value: "DEBUG")
////    jvmarg(value: "-Xss${ant.project.properties.'gwtCompilerXss'}")
////    jvmarg(value: "-Djava.awt.headless=true")
//}
////
