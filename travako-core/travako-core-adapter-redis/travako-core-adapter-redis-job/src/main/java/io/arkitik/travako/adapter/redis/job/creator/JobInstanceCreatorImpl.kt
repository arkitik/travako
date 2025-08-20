package io.arkitik.travako.adapter.redis.job.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.redis.job.TravakoJobInstance
import io.arkitik.travako.entity.redis.server.TravakoServer
import io.arkitik.travako.store.job.creator.JobInstanceCreator
import java.time.LocalDateTime
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:20 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobInstanceCreatorImpl : JobInstanceCreator {
    private lateinit var jobKey: String
    private lateinit var jobClassName: String
    private lateinit var server: ServerDomain
    private lateinit var jobTrigger: String
    private lateinit var jobTriggerType: JobInstanceTriggerType
    private var nextExecutionTime: LocalDateTime? = null

    private var jobStatus: JobStatus = JobStatus.WAITING
    private var uuid: String = UUID.randomUUID().toString().replace("-", "")
    private var singleRun: Boolean = false

    override fun String.jobKey(): JobInstanceCreator {
        jobKey = this
        return this@JobInstanceCreatorImpl
    }

    override fun String.jobClassName(): JobInstanceCreator {
        jobClassName = this
        return this@JobInstanceCreatorImpl
    }

    override fun ServerDomain.server(): JobInstanceCreator {
        server = this
        return this@JobInstanceCreatorImpl
    }

    override fun JobStatus.jobStatus(): JobInstanceCreator {
        jobStatus = this
        return this@JobInstanceCreatorImpl
    }

    override fun String.jobTrigger(): JobInstanceCreator {
        jobTrigger = this
        return this@JobInstanceCreatorImpl
    }

    override fun JobInstanceTriggerType.jobTriggerType(): JobInstanceCreator {
        jobTriggerType = this
        return this@JobInstanceCreatorImpl
    }

    override fun Boolean.singleRun(): JobInstanceCreator {
        this@JobInstanceCreatorImpl.singleRun = this
        return this@JobInstanceCreatorImpl
    }

    override fun LocalDateTime?.nextExecutionTime(): JobInstanceCreator {
        this@JobInstanceCreatorImpl.nextExecutionTime = this
        return this@JobInstanceCreatorImpl
    }

    override fun String.uuid(): StoreIdentityCreator<String, JobInstanceDomain> {
        uuid = this
        return this@JobInstanceCreatorImpl
    }

    override fun create() =
        TravakoJobInstance(
            jobKey = jobKey,
            jobStatus = jobStatus,
            uuid = uuid,
            travakoServer = server as TravakoServer,
            jobTrigger = jobTrigger,
            jobTriggerType = jobTriggerType,
            nextExecutionTime = nextExecutionTime,
            jobClassName = jobClassName,
            singleRun = singleRun,
            serverUuid = server.uuid,
            schedulerRunner = null,
            assignedToUuid = null,
        )
}
