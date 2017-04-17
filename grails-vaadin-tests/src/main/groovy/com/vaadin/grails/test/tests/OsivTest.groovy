package com.vaadin.grails.test.tests

import com.vaadin.data.util.BeanItemContainer
import com.vaadin.grails.test.OsivDummy1
import com.vaadin.grails.test.utils.PluginTest
import com.vaadin.grails.test.utils.PluginTestSuite
import com.vaadin.ui.Grid
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout

/**
 * OSIV (Open Session In View) tests.
 */
@PluginTestSuite
class OsivTest {

    /**
     * If you comment out "openSessionInViewFilter" in VaadinConfig.groovy this test is supposed to fail.
     *
     * Of course, if there is no OSIV filter registered, this test fails (which is what we are testingn here).
     */
    @PluginTest
    public void 'No Session found for current thread exception is not thrown'() {
        List<OsivDummy1> all = OsivDummy1.findAll()
        BeanItemContainer<OsivDummy1> container = new BeanItemContainer<OsivDummy1>(OsivDummy1.class, all)

        Grid grid = new Grid(container)

        VerticalLayout content = UI.current.content
        content.addComponent(grid)
        content.removeComponent(grid)
    }
}
