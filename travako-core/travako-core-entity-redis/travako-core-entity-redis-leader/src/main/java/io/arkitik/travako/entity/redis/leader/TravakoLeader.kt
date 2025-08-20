package io.arkitik.travako.entity.redis.leader

import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.redis.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.redis.server.TravakoServer
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Reference
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 8:00 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@RedisHash("travako:leader")
data class TravakoLeader(
    @Id
    override val uuid: String,
    @Reference
    var schedulerRunner: TravakoSchedulerRunner?,
    @Reference
    val travakoServer: TravakoServer?,
    override var lastModifiedDate: LocalDateTime = LocalDateTime.now(),
    override val creationDate: LocalDateTime = LocalDateTime.now(),
    var runnerUuid: String,
    @Indexed
    val serverUuid: String,
) : LeaderDomain {
    override val server: ServerDomain
        get() = travakoServer!!
    override val runner: SchedulerRunnerDomain
        get() = schedulerRunner!!
}