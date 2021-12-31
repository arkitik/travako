package io.arkitik.travako.operation.runner.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.store.storeCreator
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.operation.runner.roles.CheckUnRegisteredRole
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.store.runner.SchedulerRunnerStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:28 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RegisterRunnerOperationProvider(
    private val schedulerRunnerStore: SchedulerRunnerStore,
    private val serverDomainSdk: ServerDomainSdk,
) {
    val registerRunnerOperation = operationBuilder<RunnerKeyDto, Unit> {
        install(CheckUnRegisteredRole(schedulerRunnerStore.storeQuery))
        mainOperation {
            val serverDomain = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
            with(schedulerRunnerStore) {
                storeCreator(identityCreator()) {
                    runnerKey.runnerKey()
                    InstanceState.UP.instanceState()
                    serverDomain.server()
                    create()
                }.save()
            }
        }
    }

}
