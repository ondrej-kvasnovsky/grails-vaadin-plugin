package com.vaadin.grails

import org.springframework.beans.BeansException
import grails.util.Holders
import org.springframework.context.MessageSource
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Manages access to Grails services (actually beans) and i18n.
 *
 * @author Ondrej Kvasnovsky
 */
class Grails {

    private static final transient Logger log = LoggerFactory.getLogger(this)

    /**
     * Returns bean from application context. Example use: Grails.get(UserService).findAllUsers()
     *
     * @param clazz bean class
     * @return bean from the context
     * @throws BeansException
     */
    public static <T> T get(final Class<T> clazz) throws BeansException {
        ApplicationContext context = getApplicationContext()
        T res = context.getBean(clazz)
        return res
    }

    /**
     * Returns bean from application context. Example use: Grails.get('dataSource')
     *
     * @param name of the bean
     * @return bean from the context
     * @throws BeansException
     */
    public static <T> T get(final String name) throws BeansException {
        ApplicationContext context = getApplicationContext()
        T res = context.getBean(name)
        return res
    }


    /**
     * Returns Grails application context.
     *
     * @return grails application context
     */
    public static ApplicationContext getApplicationContext() {
        GrailsApplication application = Holders.getGrailsApplication()
        ApplicationContext res = application.getMainContext()
        return res
    }

    /**
     * Returns Grails message source from the application context.
     *
     * @return grails message source
     */
    public static MessageSource getMessageSource() {
        ApplicationContext context = getApplicationContext()
        MessageSource res = context.getBean('messageSource')
        return res
    }

    /**
     * Provides access to i18n values.
     *
     * @param key to localization property
     * @return value from properties file or key (if key value is not found)
     */
    public static String i18n(final String key) {
        Locale locale = LocaleContextHolder.getLocale()
        String res = i18n(key, locale)
        return res
    }

    /**
     * Provides access to i18n values.
     *
     * @param key to localization property
     * @return value from properties file or key (if key value is not found)
     */
    public static String i18n(final String key, final Locale locale) {
        String res = i18n(key, null, locale)
        return res
    }

    /**
     * Provides access to i18n values.
     *
     * @param key to localization property
     * @param args arguments, e.g. "Hallo {0}"
     * @return value from properties file or key (if key value is not found)
     */
    public static String i18n(final String key, final Object[] args) {
        String res = i18n(key, args, null, null)
        return res
    }

    /**
     * Provides access to i18n values.
     *
     * @param key to localization property
     * @param args arguments, e.g. "Hallo {0}"
     * @param locale locale
     * @return value from properties file or key (if key value is not found)
     */
    public static String i18n(final String key, final Object[] args, final Locale locale) {
        String res = i18n(key, args, null, locale)
        return res
    }

    /**
     * Provides access to i18n values.
     *
     * @param key to localization property
     * @param args arguments, e.g. "Hello {0}"
     * @param defaultValue
     * @param locale locale
     * @return value from properties file or key (if key value is not found)
     */
    public static String i18n(final String key, final Object[] args, final String defaultValue, Locale locale) {
        String res = null
        try {
            if (locale == null) {
                locale = LocaleContextHolder.getLocale()
            }
            if (defaultValue) {
                res = getMessageSource().getMessage(key, args, defaultValue, locale)
            }
            else {
                res = getMessageSource().getMessage(key, args, locale)
            }
        } catch (final Throwable t) {
            log.warn(t.getLocalizedMessage())
        }
        if (res == null) {
            // return the key in brackets in case the fetching fails
            res = "[" + key + "]"
        }
        return res
    }
}
