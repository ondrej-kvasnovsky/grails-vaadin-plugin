vaadin {

    // Your Vaadin UI classes that extends com.vaadin.ui.UI.
    // The application will be available at e.g. http://localhost:8080/grails-vaadin7-demo/
    // mapping = [
    //         "/*": "com.app.MyUI"
    // ]

    // Extra mapping in case you need 'reserve' a URL and it shouldn't be mapped to e.g. /* by Vaadin
    // mappingExtras = [
    //         '/console/*'
    // ]

    // This is optional, all packages will get scanned by default.
    packages = ['com.app']

    // This is optional, default is "com.vaadin.grails.server.DefaultUIProvider".
    // uiProvider = "com.mycompany.MyGrailsAwareUIProvider"

    // This is optional because the servlet is provided by default.
    // servletClass = "com.mycompany.MyGrailsAwareApplicationServlet"

    productionMode = false

    // Uncomment this to activate Open Session in View for Hibernate 3
    // openSessionInViewFilter = 'org.springframework.orm.hibernate3.support.OpenSessionInViewFilter'
    // ...or this for Hibernate 4
    openSessionInViewFilter = 'org.springframework.orm.hibernate4.support.OpenSessionInViewFilter'

    // Uncomment this to enable asynchronous communication, useful for vaadin-push
    // asyncSupported = true

    // Uncomment and provide name of the theme (a directory name in web-app/VAADIN/themes folder)
    // themes = ['sample']
    // You can specify exact version of Vaadin for SASS compilation
    // sassCompile = '7.1.9'

    // Use your own widgetset (e.g. include addons)
    // - add the depedencies in BuildConfig.groovy
    // - create src/java/com/mycompany/widgetset.gwt.xml 
    //   and add the widgetset from the addon there
    // - run ``grails vaadin-compile-widgetset`` afterwards 
    //   (and after major Vaadin version changes)
    // Unless set the default from Vaadin is used
    // widgetset = 'com.mycompany.widgetset'

    compile {
        //usePathJar = true //if you have problem with long classpath and vaadin-compile-widgetset on windows, uncomment this line
        //classPathRegExp="(?ismx)([^;]*(vaadin|gwt-dev|ant|sac)[^;]*);" //Regex pattern including jar contains following words vaadin|gwt-dev|ant|sac, used by default for path jar

        //memory settings
        //antMaxMemory='512m'
        //gwtCompilerXss='1024k'

        //workers=4 //by default 4 parallel workers threads used for compile
    }
}

environments {
    production {
        vaadin {
            productionMode = true
        }
    }
}
