package io.arkitik.travako.operation.runner.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.sdk.domain.leader.LeaderDomainSdk
import io.arkitik.travako.sdk.domain.leader.dto.LeaderDomainServerDto
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.runner.dto.RunnerDetails
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 01 12:04 PM, **Tue, March 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RunnerDetailsOperationProvider(
    private val leaderDomainSdk: LeaderDomainSdk,
    private val serverDomainSdk: ServerDomainSdk,
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
) {
    val runnerDetails = operationBuilder<RunnerKeyDto, RunnerDetails> {
        mainOperation {
            val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
            val leaderDomain = leaderDomainSdk.fetchServerLeader.runOperation(LeaderDomainServerDto(server))
            schedulerRunnerDomainSdk.fetchSchedulerRunner
                .runOperation(
                    RunnerDomainDto(
                        server = server,
                        runnerKey = runnerKey,
                        runnerHost = runnerHost
                    )
                ).let { runner ->
                    RunnerDetails(
                        runnerKey = runner.runnerKey,
                        runnerHost = runner.runnerHost,
                        isRunning = InstanceState.UP == runner.instanceState,
                        isLeader = leaderDomain.runner.uuid == runner.uuid,
                        lastHeartbeatTime = runner.lastHeartbeatTime
                    )
                }
        }
    }
}
