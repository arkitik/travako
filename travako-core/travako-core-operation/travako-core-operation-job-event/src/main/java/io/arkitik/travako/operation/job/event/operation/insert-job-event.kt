package io.arkitik.travako.operation.job.event.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.store.storeCreator
import io.arkitik.travako.domain.job.event.embedded.JobEventType
import io.arkitik.travako.sdk.domain.job.JobDomainSdk
import io.arkitik.travako.sdk.domain.job.dto.JobDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.job.event.dto.JobEventKeyDto
import io.arkitik.travako.store.job.event.JobEventStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:11 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class InsertJobEventOperationProvider(
    private val jobEventType: JobEventType,
    private val jobDomainSdk: JobDomainSdk,
    private val jobEventStore: JobEventStore,
    private val serverDomainSdk: ServerDomainSdk,
) {
    val insertJobEvent = operationBuilder<JobEventKeyDto, Unit> {
        mainOperation {
            val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
            val jobInstance = jobDomainSdk.fetchJobInstance.runOperation(JobDomainDto(server, jobKey))
            with(jobEventStore) {
                storeCreator(identityCreator()) {
                    jobInstance.jobInstance()
                    jobEventType.eventType()
                    create()
                }.save()
            }
        }
    }
}
