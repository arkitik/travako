package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.radix.develop.store.storeUpdater
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.operation.job.errors.JobErrors
import io.arkitik.travako.operation.job.roles.CheckJobRegisteredRole
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.store.job.JobInstanceStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 9:35 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class MarkJobAsRunningOperationProvider(
    private val jobInstanceStore: JobInstanceStore,
) {
    val markJobAsRunning = operationBuilder<JobKeyDto, Unit> {
        install(CheckJobRegisteredRole(jobInstanceStore.storeQuery))

        mainOperation {
            with(jobInstanceStore) {
                val jobInstanceDomain = storeQuery.findByServerKeyAndJobKey(
                    serverKey = serverKey, jobKey = jobKey
                ) ?: throw JobErrors.JOB_IS_NOT_REGISTERED.unprocessableEntity()
                storeUpdater(jobInstanceDomain.identityUpdater()) {
                    JobStatus.RUNNING.status()
                    update()
                }.save()
            }
        }
    }
}
