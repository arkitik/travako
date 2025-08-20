package io.arkitik.travako.port.redis.server

import io.arkitik.travako.adapter.redis.server.ServerStoreImpl
import io.arkitik.travako.adapter.redis.server.repository.TravakoServerRepository
import io.arkitik.travako.store.server.ServerStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 9:18 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class ServerRedisPortContext {

    @Bean
    fun serverStore(
        travakoServerRepository: TravakoServerRepository,
    ): ServerStore = ServerStoreImpl(travakoServerRepository)
}