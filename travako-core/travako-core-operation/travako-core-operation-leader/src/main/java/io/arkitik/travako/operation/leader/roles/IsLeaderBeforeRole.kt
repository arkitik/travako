package io.arkitik.travako.operation.leader.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.travako.sdk.leader.dto.IsLeaderBeforeDto
import io.arkitik.travako.store.leader.query.LeaderStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:16 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class IsLeaderBeforeRole(
    private val leaderStoreQuery: LeaderStoreQuery,
) : OperationRole<IsLeaderBeforeDto, Boolean> {
    override fun IsLeaderBeforeDto.operateRole() =
        leaderStoreQuery.existsByServerKeyAndRunnerKeyAndBefore(
            serverKey = serverKey,
            runnerKey = runnerKey,
            beforeDate = dateBefore
        )
}
