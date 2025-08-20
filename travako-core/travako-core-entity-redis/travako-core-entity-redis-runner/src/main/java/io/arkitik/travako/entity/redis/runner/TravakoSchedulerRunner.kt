package io.arkitik.travako.entity.redis.runner

import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.redis.server.TravakoServer
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Reference
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:06 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@RedisHash("travako:scheduler:runner")
data class TravakoSchedulerRunner(
    override val runnerKey: String,
    override var instanceState: InstanceState,
    override val runnerHost: String,
    @Id
    override val uuid: String,
    @Reference
    val travakoServer: TravakoServer?,
    override val creationDate: LocalDateTime = LocalDateTime.now(),
    override var lastHeartbeatTime: LocalDateTime? = null,
    @Indexed
    val serverUuid: String,
) : SchedulerRunnerDomain {
    override val server: ServerDomain
        get() = travakoServer!!
}
