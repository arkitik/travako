package io.arkitik.travako.adapter.redis.server.updater

import io.arkitik.travako.entity.redis.server.TravakoServer
import io.arkitik.travako.store.server.updater.ServerUpdater

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:06 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ServerUpdaterImpl(
    private val server: TravakoServer,
) : ServerUpdater {
    override fun update() = server
}
