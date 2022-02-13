package io.arkitik.travako.store.server.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.server.ServerDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:40 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface ServerCreator : StoreIdentityCreator<String, ServerDomain> {
    fun String.serverKey(): ServerCreator
}
