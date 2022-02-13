package io.arkitik.travako.domain.job.event

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.domain.job.event.embedded.JobEventType

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 03 11:29 PM, **Mon, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobEventDomain : Identity<String> {
    override val uuid: String
    val jobInstance: JobInstanceDomain
    val eventType: JobEventType
    val processedFlag: Boolean
}
