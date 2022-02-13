package io.arkitik.travako.operation.leader.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.store.storeUpdaterWithSave
import io.arkitik.travako.operation.leader.roles.ServerNotRegisteredLeaderRole
import io.arkitik.travako.sdk.domain.leader.LeaderDomainSdk
import io.arkitik.travako.sdk.domain.leader.dto.LeaderDomainServerDto
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.leader.dto.LeaderRunnerKeyDto
import io.arkitik.travako.sdk.leader.dto.LeaderServerKeyDto
import io.arkitik.travako.store.leader.LeaderStore
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:19 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SwitchServerOperationProvider(
    private val leaderStore: LeaderStore,
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
    private val serverDomainSdk: ServerDomainSdk,
    private val leaderDomainSdk: LeaderDomainSdk,
) {
    val switchLeader = operationBuilder<LeaderServerKeyDto, LeaderRunnerKeyDto> {
        install(ServerNotRegisteredLeaderRole(leaderStore.storeQuery, serverDomainSdk))
        mainOperation {
            with(leaderStore) {
                val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
                val leader = leaderDomainSdk.fetchServerLeader.runOperation(LeaderDomainServerDto(server))
                val schedulerRunner = schedulerRunnerDomainSdk.fetchOldestHeartbeatRunner.runOperation(server)
                storeUpdaterWithSave(leader.identityUpdater()) {
                    schedulerRunner.assignToRunner()
                    LocalDateTime.now().lastModifiedDate()
                    update()
                }.save()
                LeaderRunnerKeyDto(
                    serverKey = serverKey,
                    runnerKey = schedulerRunner.runnerKey,
                    runnerHost = schedulerRunner.runnerHost,
                )
            }

        }
    }
}
