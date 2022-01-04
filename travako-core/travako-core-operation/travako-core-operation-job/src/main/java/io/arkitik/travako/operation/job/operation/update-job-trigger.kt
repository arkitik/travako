package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.radix.develop.store.storeUpdater
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.operation.job.errors.JobErrors
import io.arkitik.travako.operation.job.roles.CheckJobRegisteredRole
import io.arkitik.travako.sdk.job.dto.CreateJobDto
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.sdk.job.event.dto.JobEventKeyDto
import io.arkitik.travako.store.job.JobInstanceStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:11 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class UpdateJobTriggerOperationProvider(
    private val jobInstanceStore: JobInstanceStore,
    private val jobEventSdk: JobEventSdk,
) {
    val updateJobTrigger = operationBuilder<CreateJobDto, Unit> {
        install {
            CheckJobRegisteredRole(jobInstanceStore.storeQuery).operateRole(JobKeyDto(serverKey, jobKey))
        }

        mainOperation {
            with(jobInstanceStore) {
                val jobInstanceDomain = storeQuery.findByServerKeyAndJobKey(
                    serverKey = serverKey, jobKey = jobKey
                ) ?: throw JobErrors.JOB_IS_NOT_REGISTERED.unprocessableEntity()
                storeUpdater(jobInstanceDomain.identityUpdater()) {
                    jobTrigger.jobTrigger()
                    (JobInstanceTriggerType.DURATION.takeIf { isDuration }
                        ?: JobInstanceTriggerType.CRON).jobTriggerType()
                    update()
                }.save()
            }
            jobEventSdk.insertRestartJobEvent.runOperation(JobEventKeyDto(serverKey, jobKey))
        }
    }
}
