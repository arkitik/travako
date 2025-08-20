package io.arkitik.travako.entity.redis.job.event

import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.RunnerJobEventStateDomain
import io.arkitik.travako.entity.redis.runner.TravakoSchedulerRunner
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Reference
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 03 11:42 PM, **Mon, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@RedisHash("travako:runner:job:event:state")
data class TravakoRunnerJobEventState(
    @Id
    override val uuid: String,
    @Reference
    val schedulerRunner: TravakoSchedulerRunner?,
    @Reference
    val travakoJobEvent: TravakoJobEvent?,
    override val creationDate: LocalDateTime = LocalDateTime.now(),
    @Indexed
    val runnerUuid: String,
    @Indexed
    val jobEventUuid: String,
) : RunnerJobEventStateDomain {
    override val runner: SchedulerRunnerDomain
        get() = schedulerRunner!!
    override val jobEvent: JobEventDomain
        get() = travakoJobEvent!!
}