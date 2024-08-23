package io.arkitik.travako.operation.runner.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.operation.runner.errors.RunnerErrors
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.store.runner.query.SchedulerRunnerStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:54 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class FetchSchedulerRunnerOperationProvider(
    private val schedulerRunnerStoreQuery: SchedulerRunnerStoreQuery,
) {
    val fetchSchedulerRunner = operationBuilder<RunnerDomainDto, SchedulerRunnerDomain> {
        mainOperation {
            schedulerRunnerStoreQuery.findByServerAndRunnerKeyWithHost(
                server = server,
                runnerKey = runnerKey,
                runnerHost = runnerHost,
            ).unprocessableEntity(RunnerErrors.RUNNER_IS_NOT_REGISTERED)
        }
    }
}
