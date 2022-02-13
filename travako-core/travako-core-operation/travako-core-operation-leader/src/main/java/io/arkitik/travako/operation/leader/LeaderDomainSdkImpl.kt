package io.arkitik.travako.operation.leader

import io.arkitik.travako.operation.leader.operation.FetchServerLeaderOperationProvider
import io.arkitik.travako.sdk.domain.leader.LeaderDomainSdk
import io.arkitik.travako.store.leader.LeaderStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:51 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class LeaderDomainSdkImpl(
    leaderStore: LeaderStore,
) : LeaderDomainSdk {
    override val fetchServerLeader = FetchServerLeaderOperationProvider(
        leaderStore.storeQuery
    ).fetchServerLeader
}
