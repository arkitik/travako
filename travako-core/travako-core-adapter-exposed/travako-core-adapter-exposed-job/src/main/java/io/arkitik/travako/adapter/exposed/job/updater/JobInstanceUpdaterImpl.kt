package io.arkitik.travako.adapter.exposed.job.updater

import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.entity.exposed.job.TravakoJobInstance
import io.arkitik.travako.store.job.updater.JobInstanceUpdater
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:22 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class JobInstanceUpdaterImpl(
    private val entity: TravakoJobInstance,
) : JobInstanceUpdater {
    override fun removeRunnerAssignee(): JobInstanceUpdater {
        entity.assignedToUuid = null
        return this
    }

    override fun SchedulerRunnerDomain.assignToRunner(): JobInstanceUpdater {
        entity.assignedToUuid = this.uuid
        return this@JobInstanceUpdaterImpl
    }

    override fun JobStatus.jobStatus(): JobInstanceUpdater {
        entity.jobStatus = this
        return this@JobInstanceUpdaterImpl
    }

    override fun removeNextExecutionTime(): JobInstanceUpdater {
        entity.nextExecutionTime = null
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

    override fun String.jobClassName(): JobInstanceUpdater {
        entity.jobClassName = this
        return this@JobInstanceUpdaterImpl
    }

    override fun Boolean.singleRun(): JobInstanceUpdater {
        entity.singleRun = this
        return this@JobInstanceUpdaterImpl
    }

    override fun update(): JobInstanceDomain {
        return entity
    }
}
