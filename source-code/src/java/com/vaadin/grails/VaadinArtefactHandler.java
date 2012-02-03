/*
 * Copyright 2010 Les Hazlewood
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vaadin.grails;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;

import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;
import org.codehaus.groovy.grails.commons.GrailsClassUtils;

/**
 * TODO - Class JavaDoc
 * 
 * @author Les Hazlewood
 * @since 1.2
 */
public class VaadinArtefactHandler extends ArtefactHandlerAdapter {

    private static final String VAADIN_COMPONENT_DISCOVERY_TOKEN = "vaadin";

    public static final String TYPE = "Vaadin";

    @SuppressWarnings({ "unchecked" })
    public static boolean isVaadinClass(final Class clazz) {
        if (clazz == null) {
            return false;
        }

        // its not a closure
        if (Closure.class.isAssignableFrom(clazz)) {
            return false;
        }

        if (GrailsClassUtils.isJdk5Enum(clazz)) {
            return false;
        }

        // Check to see if the class is annotated as a VaadinComponent. This
        // annotation can be added to the
        // class explicitly by the end-user or automatically during compilation
        // by the VaadinASTTransformation impl.
        // If found, we obviously consider this class a Grails Vaadin component
        // to be managed
        // TODO - NOT ENABLED: Global AST Transformations do not work at the
        // moment:
        /*
         * if (clazz.getAnnotation(VaadinComponent.class) != null) {
         * System.out.println("***** Class " + clazz.getName() +
         * " DOES have the VaadinComponent annotation."); return true; }
         */

        // Not explicilty annotated - last resort is to try a heuristic:
        // If the class (or any of its parent classes) has the word 'vaadin' in
        // its fully-qualified class name, then
        // we assume it is a Vaadin component intended to be managed (all Vaadin
        // packages start with 'com.vaadin', so
        // any UI subclasses would be discovered).
        Class testClass = clazz;
        boolean result = false;
        while ((testClass != null) && !testClass.equals(GroovyObject.class) && !testClass.equals(Object.class)) {
            if (testClass.getName().contains(VaadinArtefactHandler.VAADIN_COMPONENT_DISCOVERY_TOKEN)) {
                result = true;
                break;
            }
            testClass = testClass.getSuperclass();
        }

        return result;
    }

    public VaadinArtefactHandler() {
        super(VaadinArtefactHandler.TYPE, VaadinGrailsClass.class, DefaultVaadinGrailsClass.class, null);
    }

    @Override
    public boolean isArtefactClass(final Class clazz) {
        return VaadinArtefactHandler.isVaadinClass(clazz);
    }
}
