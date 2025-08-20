package io.arkitik.travako.entity.redis.job

import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
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
 * Created At 27 7:19 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@RedisHash("travako:job:instance")
data class TravakoJobInstance(
    override val jobKey: String,
    override var jobClassName: String,
    override var jobStatus: JobStatus,
    @Id
    override val uuid: String,
    @Reference
    val travakoServer: TravakoServer?,
    override var jobTrigger: String,
    override var jobTriggerType: JobInstanceTriggerType,
    @Reference
    var schedulerRunner: TravakoSchedulerRunner?,
    override val creationDate: LocalDateTime = LocalDateTime.now(),
    override var lastRunningTime: LocalDateTime? = null,
    override var nextExecutionTime: LocalDateTime? = null,
    override var singleRun: Boolean,
    @Indexed
    val serverUuid: String,
    var assignedToUuid: String?,
) : JobInstanceDomain {
    override val server: ServerDomain
        get() = travakoServer!!
    override val assignedTo: SchedulerRunnerDomain?
        get() = schedulerRunner
}