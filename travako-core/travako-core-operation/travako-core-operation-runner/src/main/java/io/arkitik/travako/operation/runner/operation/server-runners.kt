package io.arkitik.travako.operation.runner.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.sdk.domain.leader.LeaderDomainSdk
import io.arkitik.travako.sdk.domain.leader.dto.LeaderDomainServerDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.runner.dto.RunnerDetails
import io.arkitik.travako.sdk.runner.dto.RunnerServerKeyDto
import io.arkitik.travako.store.runner.query.SchedulerRunnerStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:54 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ServerRunnersOperationProvider(
    private val schedulerRunnerStoreQuery: SchedulerRunnerStoreQuery,
    private val leaderDomainSdk: LeaderDomainSdk,
    private val serverDomainSdk: ServerDomainSdk,
) {
    val serverRunners = operationBuilder<RunnerServerKeyDto, List<RunnerDetails>> {
        mainOperation {
            val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
            val leaderDomain = leaderDomainSdk.fetchServerLeader.runOperation(LeaderDomainServerDto(server))
            schedulerRunnerStoreQuery.findAllByServer(server)
                .map { runner ->
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
    val allRunningServerRunners = operationBuilder<RunnerServerKeyDto, List<RunnerDetails>> {
        mainOperation {
            val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
            val leaderDomain = leaderDomainSdk.fetchServerLeader.runOperation(LeaderDomainServerDto(server))
            schedulerRunnerStoreQuery.findAllByServerAndStatus(
                server = server,
                status = InstanceState.UP,
            ).map { runner ->
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
