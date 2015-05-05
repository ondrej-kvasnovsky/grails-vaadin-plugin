package com.vaadin.grails.test.tests

import com.vaadin.grails.test.utils.ClassFinder
import com.vaadin.grails.test.utils.PluginTest
import com.vaadin.grails.test.utils.PluginTestSuite
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout

import java.lang.reflect.Method

class PluginTestRunner {

    public void run() {

        VerticalLayout content = UI.current.content

        Package p = getClass().package

        ArrayList<Class<?>> clazzes = new ClassFinder().getClassesForPackage(p)

        for (Class c : clazzes) {

            if (c.isAnnotationPresent(PluginTestSuite)) {
                Method[] methods = c.methods

                for (Method method : methods) {

                    if (method.isAnnotationPresent(PluginTest)) {
                        String result = " [${c.simpleName}] ${method.name}"
                        try {
                            c.newInstance().invokeMethod(method.name, null)
                            content.addComponent(new Label("Success: " + result))
                        } catch (Throwable e) {
                            content.addComponent(new Label("Failed:  " + result + " with error: ${e.message}"))
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}
