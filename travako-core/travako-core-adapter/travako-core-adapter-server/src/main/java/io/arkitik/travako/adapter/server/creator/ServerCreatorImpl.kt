package io.arkitik.travako.adapter.server.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.server.TravakoServer
import io.arkitik.travako.store.server.creator.ServerCreator
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:05 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ServerCreatorImpl : ServerCreator {
    private lateinit var serverKey: String
    private var uuid: String = UUID.randomUUID().toString().replace("-", "")
    override fun String.serverKey(): ServerCreator {
        serverKey = this
        return this@ServerCreatorImpl
    }

    override fun String.uuid(): StoreIdentityCreator<String, ServerDomain> {
        uuid = this
        return this@ServerCreatorImpl
    }


    override fun create(): ServerDomain {
        return TravakoServer(
            uuid = uuid,
            serverKey = serverKey
        )
    }
}
