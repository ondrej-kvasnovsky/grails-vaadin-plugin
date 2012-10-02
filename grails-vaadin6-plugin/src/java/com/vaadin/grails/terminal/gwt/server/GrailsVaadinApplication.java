/**
 * 
 */
package com.vaadin.grails.terminal.gwt.server;

import org.codehaus.groovy.grails.commons.ApplicationHolder;

import com.vaadin.Application;
import com.vaadin.RootRequiresMoreInformationException;
import com.vaadin.terminal.WrappedRequest;
import com.vaadin.ui.Root;

/**
 * @author ondrejkvasnovsky
 */
public class GrailsVaadinApplication extends Application {

	@Override
	protected Root getRoot(WrappedRequest request)
			throws RootRequiresMoreInformationException {
		String rootClassName = getRootClassName(request);
		try {
			ClassLoader classLoader = ApplicationHolder.getApplication().getClassLoader();
			Class<? extends Root> rootClass = classLoader.loadClass(rootClassName)
					.asSubclass(Root.class);
			try {
				Root root = rootClass.newInstance();
				return root;
			} catch (Exception e) {
				throw new RuntimeException("Could not instantiate root class "
						+ rootClassName, e);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Could not load root class "
					+ rootClassName, e);
		}
	}

}
