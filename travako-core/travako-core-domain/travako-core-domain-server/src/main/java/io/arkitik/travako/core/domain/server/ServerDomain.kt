package io.arkitik.travako.core.domain.server

import io.arkitik.radix.develop.identity.Identity

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 8:31 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface ServerDomain : Identity<String> {
    override val uuid: String
    val serverKey: String
}
