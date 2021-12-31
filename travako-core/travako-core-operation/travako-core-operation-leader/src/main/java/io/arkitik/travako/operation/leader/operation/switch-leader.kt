package io.arkitik.travako.operation.leader.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.radix.develop.store.storeUpdaterWithSave
import io.arkitik.travako.operation.leader.errors.LeaderErrors
import io.arkitik.travako.operation.leader.roles.ServerNotRegisteredLeaderRole
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerServerDomainDto
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
) {
    val switchLeader = operationBuilder<LeaderServerKeyDto, LeaderRunnerKeyDto> {
        install(ServerNotRegisteredLeaderRole(leaderStore.storeQuery))
        mainOperation {
            with(leaderStore) {
                val leaderDomain = leaderStore.storeQuery.findByServerKey(
                    serverKey
                ) ?: throw LeaderErrors.SERVER_LEADER_NOT_REGISTERED.unprocessableEntity()
                val schedulerRunnerDomain = schedulerRunnerDomainSdk.fetchOldestHeartbeatRunner.runOperation(
                    RunnerServerDomainDto(serverKey)
                )
                storeUpdaterWithSave(leaderDomain.identityUpdater()) {
                    schedulerRunnerDomain.assignToRunner()
                    LocalDateTime.now().lastModifiedDate()
                    update()
                }.save()
                LeaderRunnerKeyDto(
                    serverKey = serverKey,
                    runnerKey = schedulerRunnerDomain.runnerKey,
                )
            }

        }
    }
}
