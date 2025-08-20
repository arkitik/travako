package io.arkitik.travako.adapter.redis.job.event.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.RunnerJobEventStateDomain
import io.arkitik.travako.entity.redis.job.event.TravakoJobEvent
import io.arkitik.travako.entity.redis.job.event.TravakoRunnerJobEventState
import io.arkitik.travako.entity.redis.runner.TravakoSchedulerRunner
import io.arkitik.travako.store.job.event.creator.RunnerJobEventStateCreator
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 5:07 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RunnerJobEventStateCreatorImpl : RunnerJobEventStateCreator {
    private lateinit var runner: SchedulerRunnerDomain
    private lateinit var jobEvent: JobEventDomain
    private var uuid: String = UUID.randomUUID().toString().replace("-", "")

    override fun SchedulerRunnerDomain.runner(): RunnerJobEventStateCreator {
        runner = this
        return this@RunnerJobEventStateCreatorImpl
    }

    override fun JobEventDomain.jobEvent(): RunnerJobEventStateCreator {
        jobEvent = this
        return this@RunnerJobEventStateCreatorImpl
    }

    override fun String.uuid(): StoreIdentityCreator<String, RunnerJobEventStateDomain> {
        uuid = this
        return this@RunnerJobEventStateCreatorImpl
    }

    override fun create() =
        TravakoRunnerJobEventState(
            uuid = uuid,
            schedulerRunner = runner as TravakoSchedulerRunner,
            travakoJobEvent = jobEvent as TravakoJobEvent,
            runnerUuid = runner.uuid,
            jobEventUuid = jobEvent.uuid,
        )
}
