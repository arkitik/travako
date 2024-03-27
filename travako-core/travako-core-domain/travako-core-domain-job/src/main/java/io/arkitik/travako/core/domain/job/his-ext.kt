package io.arkitik.travako.core.domain.job

import kotlin.reflect.KProperty1

/**
 * Created By [**Ibrahim Al-Tamimi **](https://www.linkedin.com/in/iloom/)
 * Created At **Thursday **12**, May 2022**
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any, R> T.readInstanceProperty(propertyName: String): R? {
    val property = this::class.members
        .first { it.name == propertyName } as KProperty1<Any, *>
    return property.get(this) as? R
}
