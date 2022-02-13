package io.arkitik.travako.store.job.updater

import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:11 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobInstanceUpdater : StoreIdentityUpdater<String, JobInstanceDomain> {
    fun removeRunnerAssignee(): JobInstanceUpdater
    fun SchedulerRunnerDomain.assignToRunner(): JobInstanceUpdater

    fun JobStatus.status(): JobInstanceUpdater
    fun LocalDateTime.lastRunningTime(): JobInstanceUpdater

    fun String.jobTrigger(): JobInstanceUpdater
    fun JobInstanceTriggerType.jobTriggerType(): JobInstanceUpdater
}
