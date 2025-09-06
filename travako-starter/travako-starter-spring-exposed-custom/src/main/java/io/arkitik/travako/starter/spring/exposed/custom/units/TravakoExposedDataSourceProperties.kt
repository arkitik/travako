package io.arkitik.travako.starter.spring.exposed.custom.units

import org.springframework.beans.factory.BeanClassLoaderAware
import org.springframework.beans.factory.BeanCreationException
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.jdbc.DatabaseDriver
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.util.Assert
import org.springframework.util.ClassUtils
import org.springframework.util.StringUtils
import java.util.*
import javax.sql.DataSource

/**
 *
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
 * @author Ibrahim Al-Tamimi 
 * @since 13:10, Saturday, 06/09/2025
 **/
@ConfigurationProperties(prefix = "arkitik.travako.database.exposed.datasource")
class TravakoExposedDataSourceProperties : BeanClassLoaderAware, InitializingBean {
    var classLoader: ClassLoader? = null
        private set

    /**
     * Whether to generate a random datasource name.
     */
    var isGenerateUniqueName: Boolean = true

    /**
     * Datasource name to use if "generate-unique-name" is false. Defaults to "testdb"
     * when using an embedded database, otherwise null.
     */
    var name: String? = null

    /**
     * Fully qualified name of the DataSource implementation to use. By default, a
     * connection pool implementation is auto-detected from the classpath.
     */
    var type: Class<out DataSource?>? = null

    /**
     * Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
     */
    private var driverClassName: String? = null

    /**
     * Return the configured url or `null` if none was configured.
     *
     * @return the configured url
     * @see .determineUrl
     */
    /**
     * JDBC URL of the database.
     */
    var url: String? = null

    /**
     * Return the configured username or `null` if none was configured.
     *
     * @return the configured username
     * @see .determineUsername
     */
    /**
     * Login username of the database.
     */
    var username: String? = null

    /**
     * Return the configured password or `null` if none was configured.
     *
     * @return the configured password
     * @see .determinePassword
     */
    /**
     * Login password of the database.
     */
    var password: String? = null

    /**
     * Allows the DataSource to be managed by the container and obtained through JNDI. The
     * `URL`, `driverClassName`, `username` and `password` fields
     * will be ignored when using JNDI lookups.
     *
     * @param jndiName the JNDI name
     */
    /**
     * JNDI location of the datasource. Class, url, username and password are ignored when
     * set.
     */
    var jndiName: String? = null

    /**
     * Connection details for an embedded database. Defaults to the most suitable embedded
     * database that is available on the classpath.
     */
    var embeddedDatabaseConnection: EmbeddedDatabaseConnection? = null

    private var uniqueName: String? = null

    override fun setBeanClassLoader(classLoader: ClassLoader) {
        this.classLoader = classLoader
    }

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        if (this.embeddedDatabaseConnection == null) {
            this.embeddedDatabaseConnection = EmbeddedDatabaseConnection.get(this.classLoader)
        }
    }

    /**
     * Initialize a [DataSourceBuilder] with the state of this instance.
     *
     * @return a [DataSourceBuilder] initialized with the customizations defined on
     * this instance
     */
    fun initializeDataSourceBuilder(): DataSourceBuilder<*> {
        return DataSourceBuilder.create(this.classLoader)
            .type(this.type)
            .driverClassName(determineDriverClassName())
            .url(determineUrl())
            .username(determineUsername())
            .password(determinePassword())
    }

    /**
     * Return the configured driver or `null` if none was configured.
     *
     * @return the configured driver
     * @see .determineDriverClassName
     */
    fun getDriverClassName(): String {
        return this.driverClassName!!
    }

    fun setDriverClassName(driverClassName: String) {
        this.driverClassName = driverClassName
    }

    /**
     * Determine the driver to use based on this configuration and the environment.
     *
     * @return the driver to use
     * @since 1.4.0
     */
    fun determineDriverClassName(): String {
        val driverClassName = findDriverClassName()
        if (!StringUtils.hasText(driverClassName)) {
            throw DataSourceBeanCreationException(
                "Failed to determine a suitable driver class", this,
                this.embeddedDatabaseConnection
            )
        }
        return driverClassName
    }

    fun findDriverClassName(): String {
        if (StringUtils.hasText(this.driverClassName)) {
            Assert.state(driverClassIsLoadable()) { "Cannot load driver class: " + this.driverClassName }
            return this.driverClassName!!
        }
        var driverClassName: String? = null
        if (StringUtils.hasText(this.url)) {
            driverClassName = DatabaseDriver.fromJdbcUrl(this.url).driverClassName
        }
        if (!StringUtils.hasText(driverClassName)) {
            driverClassName = this.embeddedDatabaseConnection!!.driverClassName
        }
        return driverClassName!!
    }

    private fun driverClassIsLoadable(): Boolean {
        try {
            ClassUtils.forName(this.driverClassName!!, null)
            return true
        } catch (ex: UnsupportedClassVersionError) {
            // Driver library has been compiled with a later JDK, propagate error
            throw ex
        } catch (_: Throwable) {
            return false
        }
    }

    /**
     * Determine the url to use based on this configuration and the environment.
     *
     * @return the url to use
     * @since 1.4.0
     */
    fun determineUrl(): String? {
        if (StringUtils.hasText(this.url)) {
            return this.url
        }
        val databaseName = determineDatabaseName()
        val url = if (databaseName != null) this.embeddedDatabaseConnection!!.getUrl(databaseName) else null
        if (!StringUtils.hasText(url)) {
            throw DataSourceBeanCreationException(
                "Failed to determine suitable jdbc url", this,
                this.embeddedDatabaseConnection
            )
        }
        return url
    }

    /**
     * Determine the name to used based on this configuration.
     *
     * @return the database name to use or `null`
     * @since 2.0.0
     */
    fun determineDatabaseName(): String? {
        if (this.isGenerateUniqueName) {
            if (this.uniqueName == null) {
                this.uniqueName = UUID.randomUUID().toString()
            }
            return this.uniqueName
        }
        if (StringUtils.hasLength(this.name)) {
            return this.name
        }
        if (this.embeddedDatabaseConnection != EmbeddedDatabaseConnection.NONE) {
            return "testdb"
        }
        return null
    }

    /**
     * Determine the username to use based on this configuration and the environment.
     *
     * @return the username to use
     * @since 1.4.0
     */
    fun determineUsername(): String? {
        if (StringUtils.hasText(this.username)) {
            return this.username
        }
        if (EmbeddedDatabaseConnection.isEmbedded(findDriverClassName(), determineUrl())) {
            return "sa"
        }
        return null
    }

    /**
     * Determine the password to use based on this configuration and the environment.
     *
     * @return the password to use
     * @since 1.4.0
     */
    fun determinePassword(): String? {
        if (StringUtils.hasText(this.password)) {
            return this.password
        }
        if (EmbeddedDatabaseConnection.isEmbedded(findDriverClassName(), determineUrl())) {
            return ""
        }
        return null
    }


    internal class DataSourceBeanCreationException(
        message: String,
        val properties: TravakoExposedDataSourceProperties?,
        val connection: EmbeddedDatabaseConnection?,
    ) : BeanCreationException(message)
}
