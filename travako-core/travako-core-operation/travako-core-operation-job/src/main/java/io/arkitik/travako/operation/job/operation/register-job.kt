package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.store.creatorWithSave
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.operation.job.roles.CheckRegisteredJobRole
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.job.dto.CreateJobDto
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.store.job.JobInstanceStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:11 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RegisterJobOperationProvider(
    private val jobInstanceStore: JobInstanceStore,
    private val serverDomainSdk: ServerDomainSdk,
) {
    val registerJob = operationBuilder<CreateJobDto, Unit> {
        install {
            CheckRegisteredJobRole(
                jobInstanceStore.storeQuery,
                serverDomainSdk
            ).operateRole(JobKeyDto(serverKey = serverKey, jobKey = jobKey))
        }
        mainOperation {
            val server = serverDomainSdk.fetchServer.runOperation(
                ServerDomainDto(serverKey)
            )
            with(jobInstanceStore) {
                creatorWithSave(identityCreator()) {
                    jobKey.jobKey()
                    JobStatus.WAITING.jobStatus()
                    server.server()
                    jobTrigger.jobTrigger()
                    (JobInstanceTriggerType.DURATION.takeIf { isDuration }
                        ?: JobInstanceTriggerType.CRON).jobTriggerType()
                }
            }
        }
    }
}
