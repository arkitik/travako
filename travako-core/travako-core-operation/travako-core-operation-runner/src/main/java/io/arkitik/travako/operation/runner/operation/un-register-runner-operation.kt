package io.arkitik.travako.operation.runner.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.operation.runner.errors.RunnerErrors
import io.arkitik.travako.operation.runner.roles.CheckRegisteredRole
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.JobRunnerKeyDto
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.store.runner.SchedulerRunnerStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:28 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class UnRegisterRunnerOperationProvider(
    private val schedulerRunnerStore: SchedulerRunnerStore,
    private val jobInstanceSdk: JobInstanceSdk,
) {

    val unRegisterRunnerOperation = operationBuilder<RunnerKeyDto, Unit> {
        install(CheckRegisteredRole(schedulerRunnerStore.storeQuery))
        mainOperation {
            with(schedulerRunnerStore) {
                val schedulerRunnerDomain = storeQuery.findByRunnerKeyAndServerKey(runnerKey = runnerKey, serverKey = serverKey)
                    ?: throw RunnerErrors.RUNNER_IS_NOT_REGISTERED.unprocessableEntity()
                jobInstanceSdk.removeRunnerJobsAssignee.runOperation(JobRunnerKeyDto(
                    serverKey = serverKey,
                    runnerKey = schedulerRunnerDomain.runnerKey
                ))
                schedulerRunnerDomain.delete()
            }
        }
    }

}
