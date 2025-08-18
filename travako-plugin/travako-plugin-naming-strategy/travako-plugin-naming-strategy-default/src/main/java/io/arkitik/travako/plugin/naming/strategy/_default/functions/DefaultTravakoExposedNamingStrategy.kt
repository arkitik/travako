package io.arkitik.travako.plugin.naming.strategy._default.functions

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.travako.plugin.naming.strategy._default.config.TravakoNamingStrategyConfig
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import kotlin.reflect.KClass

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:50 PM, 18/08/2025
 */
internal class DefaultTravakoExposedNamingStrategy(
    private val travakoNamingStrategyConfig: TravakoNamingStrategyConfig,
) : TravakoExposedNamingStrategy {
    override fun provideTableName(table: KClass<out Identity<*>>): String {
        val formattedTableName = table.simpleName!!.replace(Regex("([A-Z])"), "_$1")
            .replace("-", "_")
            .lowercase()
            .removePrefix("_")
        val prefix = travakoNamingStrategyConfig.prefix ?: return formattedTableName
        return "${prefix}_${formattedTableName}"
    }
}