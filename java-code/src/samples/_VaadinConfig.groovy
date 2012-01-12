
vaadin {

    //Your Vaadin application class that extends com.vaadin.Application:
    applicationClass = "com.mycompany.MyVaadinApplication"

    autowire = "byName" //how should dependencies be injected? other option is 'byType'

    //the context relative path where you want to access your Vaadin UI.  Default is the context root
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
