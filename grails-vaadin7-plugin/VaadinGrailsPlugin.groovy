import com.vaadin.grails.VaadinConfiguration

/**
 * Plugin that integrates Grails with Vaadin 7.
 *
 * @author Ondrej Kvasnovsky
 */
class VaadinGrailsPlugin {

    private static final String DEFAULT_SERVLET = "com.vaadin.grails.GrailsVaadinServlet";

    def version = "7.1.0"
    def grailsVersion = "2.0 > *"
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def title = "Vaadin 7 Plugin"
    def author = "Ondrej Kvasnovsky"
    def authorEmail = "ondrej.kvasnovsky@gmail.com"
    def description = '''
        Grails plugin integrating Vaadin 7 into the Grails project.
        '''

    def documentation = "http://vaadinongrails.com"
    def license = "APACHE"
    def organization = [name: "Ondrej Kvasnovsky", url: "http://vaadinongrails.com"]
    def issueManagement = [system: "JIRA", url: "http://jira.grails.org/browse/GPVAADIN"]
    def scm = [url: "https://github.com/ondrej-kvasnovsky/grails-vaadin-plugin"]

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

        mapping.eachWithIndex() { obj, i ->
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
}
