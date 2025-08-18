package io.arkitik.travako.operation.runner.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.store.creatorWithInsert
import io.arkitik.radix.develop.store.storeUpdater
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.operation.runner.roles.CheckUnRegisteredRole
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.store.runner.SchedulerRunnerStore
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:28 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RegisterRunnerOperationProvider(
    private val schedulerRunnerStore: SchedulerRunnerStore,
    private val serverDomainSdk: ServerDomainSdk,
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
) {
    val registerRunnerOperation = operationBuilder<RunnerKeyDto, Unit> {
        install(CheckUnRegisteredRole(schedulerRunnerStore.storeQuery, serverDomainSdk))
        mainOperation {
            with(schedulerRunnerStore) {
                val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
                val schedulerRunner = schedulerRunnerDomainSdk.fetchSchedulerRunner
                    .runCatching {
                        runOperation(
                            RunnerDomainDto(
                                server = server,
                                runnerKey = runnerKey,
                                runnerHost = runnerHost
                            )
                        )
                    }.getOrElse {
                        creatorWithInsert(identityCreator()) {
                            runnerKey.runnerKey()
                            runnerHost.runnerHost()
                            InstanceState.UP.instanceState()
                            server.server()
                        }
                    }
                storeUpdater(schedulerRunner.identityUpdater()) {
                    InstanceState.UP.instanceState()
                    LocalDateTime.now().lastHeartbeatTime()
                    update()
                }.updateIgnore()
            }
        }
    }

}
