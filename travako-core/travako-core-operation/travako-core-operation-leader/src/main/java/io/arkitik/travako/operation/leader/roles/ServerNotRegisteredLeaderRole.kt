package io.arkitik.travako.operation.leader.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.operation.leader.errors.LeaderErrors
import io.arkitik.travako.sdk.leader.dto.LeaderServerKeyDto
import io.arkitik.travako.store.leader.query.LeaderStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:16 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ServerNotRegisteredLeaderRole(
    private val leaderStoreQuery: LeaderStoreQuery,
) : OperationRole<LeaderServerKeyDto, Unit> {
    override fun LeaderServerKeyDto.operateRole() {
        leaderStoreQuery.existsByServerKey(serverKey)
            .takeIf { !it }?.also {
                throw LeaderErrors.SERVER_LEADER_NOT_REGISTERED.unprocessableEntity()
            }
    }
}
