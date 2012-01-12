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

import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * Vaadin plugin utility methods - mostly used for supporting dynamic method.
 * 
 * @author Les Hazlewood
 * @since 1.2
 */
public class VaadinUtils {

    public static String i18n(String key, Object[] args, Locale locale) {
        return getMessageSource().getMessage(key, args, locale);
    }

    public static String i18n(String key, Object[] args, String defaultValue, Locale locale) {
        return getMessageSource().getMessage(key, args, defaultValue, locale);
    }

    public static MessageSource getMessageSource() {
        return ApplicationHolder.getApplication().getMainContext().getBean(MessageSource.class);
    }

    public static Object getBean(String name) throws BeansException {
        return ApplicationHolder.getApplication().getMainContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return ApplicationHolder.getApplication().getMainContext().getBean(clazz);
    }
}
