/*
 * Copyright 2008 Les Hazlewood
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

import java.util.Locale;

import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.MessageSource;

/**
 * Vaadin plugin utility methods - mostly used for supporting dynamic method.
 * 
 * @author Les Hazlewood
 * @since 1.2
 */
public class VaadinUtils {

    public static <T> T getBean(final Class<T> clazz) throws BeansException {
        return ApplicationHolder.getApplication().getMainContext().getBean(clazz);
    }

    public static Object getBean(final String name) throws BeansException {
        return ApplicationHolder.getApplication().getMainContext().getBean(name);
    }

    public static MessageSource getMessageSource() {
        return ApplicationHolder.getApplication().getMainContext().getBean(MessageSource.class);
    }

    /**
     * Localization methods, providing access to i18n values.
     * 
     * @param key
     *            for localization properties
     * @param args
     *            arguments, e.g. "Hallo {0}"
     * @param locale
     *            locale
     * @return value from properties file or key (if key value is not found)
     */
    public static String i18n(final String key, final Object[] args, final Locale locale) {
        String message = null;
        try {
            message = VaadinUtils.getMessageSource().getMessage(key, args, locale);
        } catch (final Throwable t) {
            System.err.println(t.getMessage());
        }
        if (message == null) {
            // if fetching values fails, return the key
            message = "[" + key + "]";
        }
        return message;
    }

    /**
     * Localization methods, providing access to i18n values.
     * 
     * @param key
     *            for localization properties
     * @param args
     *            arguments, e.g. "Hello {0}"
     * @defaultValue
     * @param locale
     *            locale
     * @return value from properties file or key (if key value is not found)
     */
    public static String i18n(final String key, final Object[] args, final String defaultValue, final Locale locale) {
        String message = null;
        try {
            message = VaadinUtils.getMessageSource().getMessage(key, args, defaultValue, locale);
        } catch (final Throwable t) {
            System.err.println(t.getMessage());
        }
        if (message == null) {
            // if fetching values fails, return the key
            message = "[" + key + "]";
        }
        return message;
    }
}
