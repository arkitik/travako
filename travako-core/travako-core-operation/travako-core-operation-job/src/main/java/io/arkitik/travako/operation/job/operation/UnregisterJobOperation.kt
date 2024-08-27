package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.resourceNotFound
import io.arkitik.radix.develop.store.storeUpdaterWithSave
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.operation.job.errors.JobErrors
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.sdk.job.event.dto.JobEventKeyDto
import io.arkitik.travako.store.job.JobInstanceParamStore
import io.arkitik.travako.store.job.JobInstanceStore

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 11:45 AM, 27/08/2024
 */
internal class UnregisterJobOperation(
    private val jobInstanceStore: JobInstanceStore,
    private val jobInstanceParamStore: JobInstanceParamStore,
    private val serverDomainSdk: ServerDomainSdk,
    private val jobEventSdk: JobEventSdk,
) : Operation<JobKeyDto, Unit> {
    override fun JobKeyDto.operate() {
        val server = serverDomainSdk.fetchServer
            .runOperation(ServerDomainDto(serverKey))
        val updatedJobInstance = with(jobInstanceStore) {
            val jobInstanceDomain = storeQuery.findByServerAndJobKey(
                server = server,
                jobKey = jobKey
            ).resourceNotFound(JobErrors.JOB_IS_NOT_REGISTERED)
            storeUpdaterWithSave(jobInstanceDomain.identityUpdater()) {
                JobStatus.DOWN.jobStatus()
                removeRunnerAssignee()
                removeNextExecutionTime()
                update()
            }
        }
        with(jobInstanceParamStore) {
            storeQuery.findAllByJobInstance(updatedJobInstance).deleteAll()
        }
        jobEventSdk.insertDeleteJobEvent.runOperation(JobEventKeyDto(serverKey, jobKey))
    }
}
