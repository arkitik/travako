package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.sdk.domain.job.JobDomainSdk
import io.arkitik.travako.sdk.domain.job.dto.JobDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.job.dto.JobDetails
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.store.job.query.JobInstanceParamStoreQuery

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 12:02 PM, 27/08/2024
 */
internal class JobDetailsOperation(
    private val serverDomainSdk: ServerDomainSdk,
    private val jobDomainSdk: JobDomainSdk,
    private val jobInstanceParamStoreQuery: JobInstanceParamStoreQuery,
) : Operation<JobKeyDto, JobDetails> {
    override fun JobKeyDto.operate(): JobDetails {
        val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
        val job = jobDomainSdk.fetchJobInstance.runOperation(JobDomainDto(server, jobKey))
        val jobInstanceParams = jobInstanceParamStoreQuery.findAllByJobInstance(job)
            .associate { it.key to it.value }
        return JobDetails(
            jobKey = job.jobKey,
            jobClassName = job.jobClassName,
            isRunning = JobStatus.RUNNING == job.jobStatus,
            jobTrigger = job.jobTrigger,
            isDuration = JobInstanceTriggerType.DURATION == job.jobTriggerType,
            lastRunningTime = job.lastRunningTime,
            params = jobInstanceParams,
        )
    }
}
