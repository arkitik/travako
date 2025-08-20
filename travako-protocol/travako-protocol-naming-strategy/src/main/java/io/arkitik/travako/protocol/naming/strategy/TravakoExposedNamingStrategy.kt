package io.arkitik.travako.protocol.naming.strategy

import io.arkitik.radix.develop.identity.Identity
import kotlin.reflect.KClass

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 10:06 AM, 17/08/2025
 */
fun interface TravakoExposedNamingStrategy {
    fun provideTableName(table: KClass<out Identity<*>>): String
}