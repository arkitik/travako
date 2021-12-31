package io.arkitik.travako.operation.runner.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.operation.runner.errors.RunnerErrors
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.store.runner.query.SchedulerRunnerStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:03 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class CheckRegisteredRole(
    private val schedulerRunnerStoreQuery: SchedulerRunnerStoreQuery,
) : OperationRole<RunnerKeyDto, Unit> {
    override fun RunnerKeyDto.operateRole() {
        schedulerRunnerStoreQuery.existsRunnerByKeyAndServerKey(runnerKey, serverKey)
            .takeIf { !it }?.also {
                throw RunnerErrors.RUNNER_IS_NOT_REGISTERED.unprocessableEntity()
            }
    }
}
