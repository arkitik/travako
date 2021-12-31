package io.arkitik.travako.operation.leader.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.shared.ext.internal
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.operation.leader.errors.LeaderErrors
import io.arkitik.travako.sdk.leader.dto.LeaderRunnerDetails
import io.arkitik.travako.sdk.leader.dto.LeaderServerKeyDto
import io.arkitik.travako.store.leader.query.LeaderStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 10:39 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class CurrentLeaderOperationProvider(
    private val leaderStoreQuery: LeaderStoreQuery,
) {
    val currentLeader = operationBuilder<LeaderServerKeyDto, LeaderRunnerDetails> {
        mainOperation {
            val leaderDomain = leaderStoreQuery.findByServerKey(serverKey)
                ?: throw LeaderErrors.SERVER_LEADER_NOT_REGISTERED.internal()
            LeaderRunnerDetails(
                serverKey,
                leaderDomain.runner.runnerKey,
                leaderDomain.runner.lastHeartbeatTime,
                InstanceState.UP == leaderDomain.runner.instanceState
            )
        }
    }
}
