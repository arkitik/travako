package io.arkitik.travako.operation.leader.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.sdk.domain.leader.LeaderDomainSdk
import io.arkitik.travako.sdk.domain.leader.dto.LeaderDomainServerDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.leader.dto.LeaderRunnerDetails
import io.arkitik.travako.sdk.leader.dto.LeaderServerKeyDto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 10:39 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class CurrentLeaderOperationProvider(
    private val serverDomainSdk: ServerDomainSdk,
    private val leaderDomainSdk: LeaderDomainSdk,
) {
    val currentLeader = operationBuilder<LeaderServerKeyDto, LeaderRunnerDetails> {
        mainOperation {
            val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
            val leader = leaderDomainSdk.fetchServerLeader.runOperation(LeaderDomainServerDto(server))
            LeaderRunnerDetails(
                serverKey = serverKey,
                runnerKey = leader.runner.runnerKey,
                runnerHost = leader.runner.runnerHost,
                lastHeartbeat = leader.runner.lastHeartbeatTime,
                isRunning = InstanceState.UP == leader.runner.instanceState
            )
        }
    }
}
