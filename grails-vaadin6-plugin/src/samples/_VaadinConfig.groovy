
vaadin {

    // Your Vaadin application class that extends com.vaadin.Application:
    applicationClass = "com.mycompany.MyVaadinApplication"

	// This is optional, GrailsAwareApplicationServlet is provided by default. Use this if you need to add or change application servlet. 
	// You should extend GrailsAwareApplicationServlet or GrailsAwareGAEApplicationServlet (from com.vaadin.grails.terminal.gwt.server package).
	// servletClass = "com.mycompany.MyGrailsAwareApplicationServlet"

    // This is optional, if the Navigator7 WebApplicationClass to be started is provided then
    // you can provide it here.  It will result in the following in the web.xml
    /*
    <init-param>
        <description>Navigator7 WebApplication class to start (optional)</description>
        <param-name>webApplication</param-name>
        <param-value>com.example.ui.application.RapWebApplication</param-value>
    </init-param>
    */
    // webApplicationClass = "com.example.ui.application.RapWebApplication"

    autowire = "byName" //how should dependencies be injected? other option is 'byType'

    // The context relative path where you want to access your Vaadin UI. Default is the context root.
    contextRelativePath = "/"
              
    productionMode = false

    googleAppEngineMode = false
}

environments {
    production {
        vaadin {
            productionMode = true
        }
    }
}
