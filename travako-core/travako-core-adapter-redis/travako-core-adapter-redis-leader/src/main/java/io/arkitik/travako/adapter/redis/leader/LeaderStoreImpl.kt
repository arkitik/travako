package io.arkitik.travako.adapter.redis.leader

import io.arkitik.radix.adapter.shared.StoreImpl
import io.arkitik.travako.adapter.redis.leader.creator.LeaderCreatorImpl
import io.arkitik.travako.adapter.redis.leader.query.LeaderStoreQueryImpl
import io.arkitik.travako.adapter.redis.leader.repository.TravakoLeaderRepository
import io.arkitik.travako.adapter.redis.leader.updater.LeaderUpdaterImpl
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.entity.redis.leader.TravakoLeader
import io.arkitik.travako.store.leader.LeaderStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:50 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class LeaderStoreImpl(
    travakoLeaderRepository: TravakoLeaderRepository,
) : StoreImpl<String, LeaderDomain, TravakoLeader>(travakoLeaderRepository), LeaderStore {
    override fun LeaderDomain.map() = this as TravakoLeader

    override val storeQuery =
        LeaderStoreQueryImpl(travakoLeaderRepository)

    override fun identityCreator() =
        LeaderCreatorImpl()

    override fun LeaderDomain.identityUpdater() =
        LeaderUpdaterImpl(map())
}
