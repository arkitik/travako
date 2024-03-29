package io.arkitik.travako.operation.runner.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.store.runner.query.SchedulerRunnerStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:54 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class FetchServerSchedulerRunnerOperationProvider(
    private val schedulerRunnerStoreQuery: SchedulerRunnerStoreQuery,
) {
    val fetchServerSchedulerRunners = operationBuilder<ServerDomain, List<SchedulerRunnerDomain>> {
        mainOperation {
            with(schedulerRunnerStoreQuery) {
                findAllByServer(
                    server = this@mainOperation
                )
            }
        }
    }
}
