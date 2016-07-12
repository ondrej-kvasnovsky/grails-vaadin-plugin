package com.vaadin.grails

import grails.util.Holders
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.BeansException
import org.springframework.beans.factory.NoUniqueBeanDefinitionException
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

/**
 * Manages access to Grails services (actually beans) and i18n.
 *
 * @author Ondrej Kvasnovsky
 */
@CompileStatic
@Slf4j
class Grails {

    /**
     * Returns bean from application context. Example use: Grails.get(UserService).findAllUsers()
     *
     * @param clazz bean class
     * @return bean from the context
     * @throws BeansException
     */
    static <T> T get(final Class<T> clazz) throws BeansException {
	    applicationContext.getBean(clazz)
    }

    /**
     * Returns bean from application context. Example use: Grails.get('dataSource')
     *
     * @param name of the bean
     * @return bean from the context
     * @throws BeansException
     */
    static <T> T get(final String name) throws BeansException {
	    (T) applicationContext.getBean(name)
    }


    /**
     * Returns Spring ApplicationContext.
     *
     * @return Spring ApplicationContext
     */
    static ApplicationContext getApplicationContext() {
        Holders.getGrailsApplication().mainContext
    }

    /**
     * Returns Grails message source from the application context.
     *
     * @return grails message source
     */
    static MessageSource getMessageSource() {
	    applicationContext.getBean('messageSource', MessageSource)
    }

    /**
     * Provides access to i18n values.
     *
     * @param key to localization property
     * @return value from properties file or key (if key value is not found)
     */
    static String i18n(final String key, final Locale locale) {
        i18n(key, null, locale)
    }

    /**
     * Provides access to i18n values.
     *
     * @param key to localization property
     * @param args arguments, e.g. "Hallo {0}"
     * @return value from properties file or key (if key value is not found)
     */
    public static String i18n(final String key, final Object[] args, final Locale locale) {
        i18n(key, args, null, locale)
    }

    /**
     * Provides access to i18n values.
     *
     * @param key to localization property
     * @param args arguments, e.g. "Hello {0}"
     * @return value from properties file or key (if key value is not found)
     */
    static String i18n(final String key, final Object[] args = null, final String defaultValue = null, Locale locale = LocaleContextHolder.locale) {
        String res
        try {
            if (defaultValue) {
                res = messageSource.getMessage(key, args, defaultValue, locale)
            }
            else {
                res = messageSource.getMessage(key, args, locale)
            }
        } catch (final Throwable t) {
            log.warn(t.localizedMessage)
        }
        if (res == null) {
            // return the key in brackets in case the fetching fails
            "[" + key + "]"
        }
        else {
            res
        }
    }

    /**
     * Returns an unique bean name for the specified type.
     *
     * @throws NoUniqueBeanDefinitionException
     */
    static String getUniqueBeanName(Class type) throws NoUniqueBeanDefinitionException {
        def beanNames = applicationContext.getBeanNamesForType(type)
        if (beanNames.size() > 1) {
            throw new NoUniqueBeanDefinitionException(type, beanNames)
        }
        beanNames.length > 0 ? beanNames.first() : null
    }

    /**
     * Returns an unique bean for the specified type.
     * @throws NoUniqueBeanDefinitionException
     */
    static <T> T getUniqueBean(Class<? super T> type, Object... args = null) throws NoUniqueBeanDefinitionException {
	    (T) applicationContext.getBean(getUniqueBeanName(type), args)
    }
}
