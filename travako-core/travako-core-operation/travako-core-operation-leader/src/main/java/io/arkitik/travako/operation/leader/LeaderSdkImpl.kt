package io.arkitik.travako.operation.leader

import io.arkitik.travako.operation.leader.operation.CurrentLeaderOperationProvider
import io.arkitik.travako.operation.leader.operation.RegisterLeaderServerOperationProvider
import io.arkitik.travako.operation.leader.operation.SwitchServerOperationProvider
import io.arkitik.travako.operation.leader.roles.IsLeaderBeforeRole
import io.arkitik.travako.sdk.domain.leader.LeaderDomainSdk
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.leader.LeaderSdk
import io.arkitik.travako.store.leader.LeaderStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:13 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class LeaderSdkImpl(
    leaderStore: LeaderStore,
    serverDomainSdk: ServerDomainSdk,
    schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
    leaderDomainSdk: LeaderDomainSdk,
) : LeaderSdk {
    override val registerLeaderServer =
        RegisterLeaderServerOperationProvider(
            leaderStore = leaderStore,
            serverDomainSdk = serverDomainSdk,
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
        ).registerLeaderServer
    override val switchLeader =
        SwitchServerOperationProvider(
            leaderStore = leaderStore,
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
            serverDomainSdk = serverDomainSdk,
            leaderDomainSdk = leaderDomainSdk
        ).switchLeader

    override val isLeaderBefore = IsLeaderBeforeRole(
        leaderStoreQuery = leaderStore.storeQuery,
        serverDomainSdk = serverDomainSdk,
        schedulerRunnerDomainSdk = schedulerRunnerDomainSdk
    )

    override val currentLeader =
        CurrentLeaderOperationProvider(
            serverDomainSdk = serverDomainSdk,
            leaderDomainSdk = leaderDomainSdk
        ).currentLeader
}
