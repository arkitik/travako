package io.arkitik.travako.store.job.event.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.embedded.JobEventType

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:11 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobEventCreator : StoreIdentityCreator<String, JobEventDomain> {
    fun JobInstanceDomain.jobInstance(): JobEventCreator
    fun JobEventType.eventType(): JobEventCreator
}
