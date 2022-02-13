package io.arkitik.travako.operation.runner.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.operation.runner.errors.RunnerErrors
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.store.runner.query.SchedulerRunnerStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:03 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class CheckUnRegisteredRole(
    private val schedulerRunnerStoreQuery: SchedulerRunnerStoreQuery,
    private val serverDomainSdk: ServerDomainSdk,
) : OperationRole<RunnerKeyDto, Unit> {
    override fun RunnerKeyDto.operateRole() {
        val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
        schedulerRunnerStoreQuery.existsServerAndRunnerByKeyAndHostAndStatus(
            server = server,
            runnerKey = runnerKey,
            runnerHost = runnerHost,
            status = InstanceState.UP
        ).takeIf { it }?.also {
            throw RunnerErrors.RUNNER_ALREADY_REGISTERED.unprocessableEntity()
        }
    }
}
