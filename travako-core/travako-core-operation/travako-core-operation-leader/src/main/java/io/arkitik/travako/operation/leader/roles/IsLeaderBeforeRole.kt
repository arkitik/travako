package io.arkitik.travako.operation.leader.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.leader.dto.IsLeaderBeforeDto
import io.arkitik.travako.store.leader.query.LeaderStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:16 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class IsLeaderBeforeRole(
    private val leaderStoreQuery: LeaderStoreQuery,
    private val serverDomainSdk: ServerDomainSdk,
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
) : OperationRole<IsLeaderBeforeDto, Boolean> {
    override fun IsLeaderBeforeDto.operateRole(): Boolean {
        val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
        val schedulerRunner = schedulerRunnerDomainSdk.fetchSchedulerRunner
            .runOperation(
                RunnerDomainDto(
                    server = server,
                    runnerKey = runnerKey,
                    runnerHost = runnerHost
                )
            )
        return leaderStoreQuery.existsByServerAndRunnerAndBefore(
            server = server,
            runner = schedulerRunner,
            beforeDate = dateBefore
        )
    }
}
