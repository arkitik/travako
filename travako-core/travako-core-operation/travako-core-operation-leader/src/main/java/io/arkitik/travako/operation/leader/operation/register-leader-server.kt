package io.arkitik.travako.operation.leader.operation

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.store.storeCreator
import io.arkitik.travako.operation.leader.roles.ServerAlreadyHaveRegisteredLeaderRole
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.leader.dto.LeaderRunnerKeyDto
import io.arkitik.travako.sdk.leader.dto.LeaderServerKeyDto
import io.arkitik.travako.store.leader.LeaderStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:14 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RegisterLeaderServerOperationProvider(
    private val leaderStore: LeaderStore,
    private val serverDomainSdk: ServerDomainSdk,
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
) {
    val registerLeaderServer = operationBuilder<LeaderRunnerKeyDto, Unit> {
        install {
            ServerAlreadyHaveRegisteredLeaderRole(
                leaderStoreQuery = leaderStore.storeQuery,
                serverDomainSdk = serverDomainSdk
            ).operateRole(LeaderServerKeyDto(serverKey))
        }
        mainOperation {
            val server = serverDomainSdk.fetchServer.runOperation(
                ServerDomainDto(serverKey)
            )
            val schedulerRunner = schedulerRunnerDomainSdk.fetchSchedulerRunner.runOperation(
                RunnerDomainDto(
                    server = server,
                    runnerKey = runnerKey,
                    runnerHost = runnerHost
                )
            )
            with(leaderStore) {
                storeCreator(identityCreator()) {
                    server.server()
                    schedulerRunner.runner()
                    create()
                }.save()
            }
        }
    }
}
