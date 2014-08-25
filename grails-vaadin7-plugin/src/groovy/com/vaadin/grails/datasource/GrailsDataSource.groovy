package com.vaadin.grails.datasource

import com.vaadin.grails.Grails
import org.apache.commons.dbcp.BasicDataSource
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.jdbc.datasource.DelegatingDataSource

import javax.sql.DataSource
import java.sql.Connection
import java.sql.SQLException

class GrailsDataSource extends DelegatingDataSource {

    private static final Log log = LogFactory.getLog(GrailsDataSource)

    private boolean _initialized

    @Override
    Connection getConnection() throws SQLException {
        initialize()
        return super.getConnection()
    }

    @Override
    void afterPropertiesSet() {
        // override to not check for targetDataSource since it's lazily created
    }

    private synchronized void initialize() {
        if (_initialized) {
            return
        }

        GrailsApplication grailsApplication = Grails.get(GrailsApplication)
        def dataSourceConfig = grailsApplication.config.dataSource

        DataSource ds = createDatasource(dataSourceConfig)
        setTargetDataSource(ds)

        _initialized = true
    }

    DataSource createDatasource(def dataSourceConfig) {

        BasicDataSource ds = new BasicDataSource()

        boolean readOnly = dataSourceConfig.readOnly
        ds.defaultReadOnly = readOnly

        boolean pooled = dataSourceConfig.pooled
        ds.poolPreparedStatements = pooled

        String driverClassName = dataSourceConfig.driverClassName ?: "org.h2.Driver"
        ds.driverClassName = driverClassName

        String username = dataSourceConfig.username
        ds.username = username

        String password = dataSourceConfig.password ?: ""
        ds.password = password

        String url = dataSourceConfig.url
        ds.url = url

        def properties = dataSourceConfig.properties

        if (properties) {

            log.debug("Setting properties on dataSource.")

            if (properties instanceof Map) {
                for (entry in properties) {
                    String key = entry.key
                    if (ds.hasProperty(key)) {
                        log.debug("Setting property on dataSource bean $key  -> $entry.value")

                        ds."$key" = entry.value

                    } else {
                        log.warn("$key  does not exist in BasicDataSource and is therefore ignored")
                    }
                }
            } else {
                log.warn("dataSource.properties is not an instanceof java.util.Map, ignoring")
            }
        }

        return ds
    }
}