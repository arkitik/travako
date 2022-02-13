package io.arkitik.travako.operation.leader.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.operation.leader.errors.LeaderErrors
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.leader.dto.LeaderServerKeyDto
import io.arkitik.travako.store.leader.query.LeaderStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:16 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ServerAlreadyHaveRegisteredLeaderRole(
    private val leaderStoreQuery: LeaderStoreQuery,
    private val serverDomainSdk: ServerDomainSdk,
) : OperationRole<LeaderServerKeyDto, Unit> {
    override fun LeaderServerKeyDto.operateRole() {
        val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
        leaderStoreQuery.existsByServer(server)
            .takeIf { it }?.also {
                throw LeaderErrors.SERVER_LEADER_ALREADY_REGISTERED.unprocessableEntity()
            }
    }
}
