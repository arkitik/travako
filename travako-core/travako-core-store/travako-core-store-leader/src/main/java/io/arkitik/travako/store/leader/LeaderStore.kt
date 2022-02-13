package io.arkitik.travako.store.leader

import io.arkitik.radix.develop.store.Store
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.store.leader.creator.LeaderCreator
import io.arkitik.travako.store.leader.query.LeaderStoreQuery
import io.arkitik.travako.store.leader.updater.LeaderUpdater

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:38 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface LeaderStore : Store<String, LeaderDomain> {
    override val storeQuery: LeaderStoreQuery

    override fun identityCreator(): LeaderCreator
    override fun LeaderDomain.identityUpdater(): LeaderUpdater
}
