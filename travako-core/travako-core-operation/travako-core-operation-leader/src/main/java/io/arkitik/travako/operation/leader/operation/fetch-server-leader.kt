package io.arkitik.travako.operation.leader.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.operation.leader.errors.LeaderErrors
import io.arkitik.travako.sdk.domain.leader.dto.LeaderDomainServerDto
import io.arkitik.travako.store.leader.query.LeaderStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:24 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class FetchServerLeaderOperationProvider(
    private val leaderStoreQuery: LeaderStoreQuery,
) {
    val fetchServerLeader = operationBuilder<LeaderDomainServerDto, LeaderDomain> {
        mainOperation {
            with(leaderStoreQuery) {
                findByServerKey(serverKey) ?: throw LeaderErrors.SERVER_LEADER_NOT_REGISTERED.unprocessableEntity()
            }
        }
    }
}
