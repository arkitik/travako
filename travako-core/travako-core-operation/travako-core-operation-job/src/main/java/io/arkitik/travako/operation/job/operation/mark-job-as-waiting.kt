package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.store.storeUpdater
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.operation.job.roles.CheckJobRegisteredRole
import io.arkitik.travako.sdk.domain.job.JobDomainSdk
import io.arkitik.travako.sdk.domain.job.dto.JobDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.job.dto.UpdateJobRequest
import io.arkitik.travako.store.job.JobInstanceStore
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 9:35 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class MarkJobAsWaitingOperationProvider(
    private val jobInstanceStore: JobInstanceStore,
    private val serverDomainSdk: ServerDomainSdk,
    private val jobDomainSdk: JobDomainSdk,
) {
    private val checkJobRegisteredRole = CheckJobRegisteredRole(jobInstanceStore.storeQuery, serverDomainSdk)
    val markJobAsWaiting = operationBuilder<UpdateJobRequest, Unit> {
        install {
            checkJobRegisteredRole.operateRole(jobKey)
        }

        mainOperation {
            val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(jobKey.serverKey))

            with(jobInstanceStore) {
                val jobInstance = jobDomainSdk.fetchJobInstance
                    .runOperation(JobDomainDto(server, jobKey.jobKey))
                storeUpdater(jobInstance.identityUpdater()) {
                    JobStatus.WAITING.status()
                    LocalDateTime.now().lastRunningTime()
                    nextExecutionTime.nextExecutionTime()
                    update()
                }.save()
            }
        }
    }
}
