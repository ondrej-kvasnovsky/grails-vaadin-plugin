import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.vaadin.grails.VaadinConfiguration

/**
 * Plugin that integrates Grails with Vaadin 7.
 *
 * @author Ondrej Kvasnovsky
 */
class VaadinGrailsPlugin {

    private static final transient Logger log = LoggerFactory.getLogger(this)

    private static final String DEFAULT_SERVLET = "com.vaadin.grails.GrailsVaadinServlet";

    // the plugin version
    def version = "1.7.0-beta9"

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0 > *"

    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def title = "Vaadin 7 Plugin" // Headline display name of the plugin
    def author = "Ondrej Kvasnovsky"
    def authorEmail = "ondrej.kvasnovsky@gmail.com"
    def description = '''
        Grails plugin integrating Vaadin 7 into the grails project.
        '''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/vaadin"

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [name: "Ondrej Kvasnovsky", url: "http://ondrej-kvasnovsky.blogspot.com"]

    // Any additional developers beyond the author specified above.
    // def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [system: "JIRA", url: "http://jira.grails.org/browse/GPVAADIN"]

    // Online location of the plugin's browseable source code.
    def scm = [url: "https://github.com/ondrej-kvasnovsky/grails-vaadin-plugin"]

    // manages access to user defined VaadinConfig
    VaadinConfiguration vaadinConfiguration = new VaadinConfiguration()

    def doWithWebDescriptor = { xml ->

        def config = vaadinConfiguration.getConfig()

        if (!config || !(config.mapping)) {
            return
        }

        def vaadinProductionMode = config.productionMode

        def contextParams = xml."context-param"
        contextParams[contextParams.size() - 1] + {
            "context-param" {
                "description"("Vaadin production mode")
                "param-name"("productionMode")
                "param-value"(vaadinProductionMode)
            }
        }

        Map mapping = config.mapping

        def applicationServlet = config.servletClass ?: DEFAULT_SERVLET
        def servletName = "VaadinServlet "
        def widgetset = config.widgetset

        def servlets = xml."servlet"

        def lastServletDefinition = servlets[servlets.size() - 1]

        mapping.eachWithIndex() { obj, i ->

            lastServletDefinition + {
                "servlet" {
                    "servlet-name"(servletName + i)
                    "servlet-class"(applicationServlet)
                    "init-param" {
                        "description"("Vaadin UI class")
                        "param-name"("UI")
                        "param-value"(obj.value)
                    }

                    if (widgetset) {
                        "init-param" {
                            "description"("Application widgetset")
                            "param-name"("widgetset")
                            "param-value"(widgetset)
                        }
                    }

                    "load-on-startup"("1")
                }
            }

        }

        def servletMappings = xml."servlet-mapping"

        def lastServletMapping = servletMappings[servletMappings.size() - 1]

        mapping.eachWithIndex() {  obj, i ->
            lastServletMapping + {
                "servlet-mapping" {
                    "servlet-name"(servletName + i)
                    "url-pattern"(obj.key)
                }
            }
        }

        servletMappings[servletMappings.size() - 1] + {
            "servlet-mapping" {
                "servlet-name"(servletName + 0)
                "url-pattern"("/VAADIN/*")
            }
        }
    }

    def doWithSpring = {
    }

    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { applicationContext ->
    }

    def onChange = { event ->
    }

    def onConfigChange = { event ->
    }

    def onShutdown = { event ->
    }
}
