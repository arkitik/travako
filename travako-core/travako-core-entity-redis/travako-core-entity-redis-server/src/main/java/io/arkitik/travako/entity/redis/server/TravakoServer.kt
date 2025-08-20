package io.arkitik.travako.entity.redis.server

import io.arkitik.travako.core.domain.server.ServerDomain
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:19 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@RedisHash("travako:server")
data class TravakoServer(
    @Id
    override val uuid: String,
    @Indexed
    override val serverKey: String,
    override val creationDate: LocalDateTime = LocalDateTime.now(),
) : ServerDomain
