package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.store.storeUpdater
import io.arkitik.travako.sdk.job.dto.JobRunnerKeyDto
import io.arkitik.travako.store.job.JobInstanceStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 6:51 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RemoveRunnerJobsAssigneeOperationProvider(
    private val jobInstanceStore: JobInstanceStore,
) {
    val removeRunnerJobsAssignee = operationBuilder<JobRunnerKeyDto, Unit> {
        mainOperation {
            with(jobInstanceStore) {
                storeQuery.findAllByServerKeyAndRunnerKey(
                    serverKey = serverKey, runnerKey = runnerKey
                ).map {
                    storeUpdater(it.identityUpdater()) {
                        removeRunnerAssignee()
                        update()
                    }
                }.save()
            }
        }
    }
}
