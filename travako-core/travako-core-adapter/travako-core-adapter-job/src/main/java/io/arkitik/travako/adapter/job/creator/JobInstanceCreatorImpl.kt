package io.arkitik.travako.adapter.job.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.job.TravakoJobInstance
import io.arkitik.travako.entity.server.TravakoServer
import io.arkitik.travako.store.job.creator.JobInstanceCreator
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:20 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobInstanceCreatorImpl : JobInstanceCreator {
    private lateinit var jobKey: String
    private lateinit var server: ServerDomain
    private lateinit var jobTrigger: String
    private lateinit var jobTriggerType: JobInstanceTriggerType

    private var jobStatus: JobStatus = JobStatus.WAITING
    private var uuid: String = UUID.randomUUID().toString().replace("-", "")

    override fun String.jobKey(): JobInstanceCreator {
        jobKey = this
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

    override fun String.uuid(): StoreIdentityCreator<String, JobInstanceDomain> {
        uuid = this
        return this@JobInstanceCreatorImpl
    }

    override fun create() =
        TravakoJobInstance(
            jobKey = jobKey,
            jobStatus = jobStatus,
            uuid = uuid,
            server = server as TravakoServer,
            jobTrigger = jobTrigger,
            jobTriggerType = jobTriggerType,
        )
}
