package io.arkitik.travako.store.job.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.server.ServerDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:11 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobInstanceCreator : StoreIdentityCreator<String, JobInstanceDomain> {
    fun String.jobKey(): JobInstanceCreator
    fun ServerDomain.server(): JobInstanceCreator
    fun JobStatus.jobStatus(): JobInstanceCreator
    fun String.jobTrigger(): JobInstanceCreator
    fun JobInstanceTriggerType.jobTriggerType(): JobInstanceCreator
}
