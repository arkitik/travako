package io.arkitik.travako.sdk.domain.job

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.sdk.domain.job.dto.JobDomainDto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:07 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobDomainSdk {
    val fetchJobInstance: Operation<JobDomainDto, JobInstanceDomain>
}
