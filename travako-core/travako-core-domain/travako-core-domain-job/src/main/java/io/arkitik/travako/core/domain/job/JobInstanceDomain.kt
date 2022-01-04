package io.arkitik.travako.core.domain.job

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 5:15 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobInstanceDomain : Identity<String> {
    override val uuid: String
    val jobKey: String
    val server: ServerDomain
    val jobStatus: JobStatus
    val jobTrigger: String
    val jobTriggerType: JobInstanceTriggerType
    val assignedTo: SchedulerRunnerDomain?
    val lastRunningTime: LocalDateTime?
}
