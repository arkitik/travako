package io.arkitik.travako.adapter.redis.job.updater

import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.entity.redis.job.TravakoJobInstance
import io.arkitik.travako.entity.redis.runner.TravakoSchedulerRunner
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
        entity.schedulerRunner = null
        return this
    }

    override fun SchedulerRunnerDomain.assignToRunner(): JobInstanceUpdater {
        entity.schedulerRunner = this as TravakoSchedulerRunner
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

    override fun update() = entity
}
