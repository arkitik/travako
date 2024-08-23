package io.arkitik.travako.adapter.job.updater

import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.entity.job.TravakoJobInstance
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.store.job.updater.JobInstanceUpdater
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:22 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobInstanceUpdaterImpl(
    private val entity: TravakoJobInstance,
) : JobInstanceUpdater {
    override fun removeRunnerAssignee(): JobInstanceUpdater {
        entity.assignedTo = null
        return this
    }

    override fun SchedulerRunnerDomain.assignToRunner(): JobInstanceUpdater {
        entity.assignedTo = this as TravakoSchedulerRunner
        return this@JobInstanceUpdaterImpl
    }

    override fun JobStatus.status(): JobInstanceUpdater {
        entity.jobStatus = this
        return this@JobInstanceUpdaterImpl
    }

    override fun LocalDateTime?.lastRunningTime(): JobInstanceUpdater {
        entity.lastRunningTime = this
        return this@JobInstanceUpdaterImpl
    }

    override fun LocalDateTime?.nextExecutionTime(): JobInstanceUpdater {
        entity.nextExecutionTime = this
        return this@JobInstanceUpdaterImpl
    }

    override fun String.jobTrigger(): JobInstanceUpdater {
        entity.jobTrigger = this
        return this@JobInstanceUpdaterImpl
    }

    override fun JobInstanceTriggerType.jobTriggerType(): JobInstanceUpdater {
        entity.jobTriggerType = this
        return this@JobInstanceUpdaterImpl
    }

    override fun update() = entity
}
