package io.arkitik.travako.adapter.job.event

import io.arkitik.radix.adapter.shared.StoreImpl
import io.arkitik.travako.adapter.job.event.creator.JobEventCreatorImpl
import io.arkitik.travako.adapter.job.event.query.JobEventStoreQueryImpl
import io.arkitik.travako.adapter.job.event.repository.TravakoJobEventRepository
import io.arkitik.travako.adapter.job.event.updater.JobEventUpdaterImpl
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.entity.job.event.TravakoJobEvent
import io.arkitik.travako.store.job.event.JobEventStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 4:50 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobEventStoreImpl(
    travakoJobEventRepository: TravakoJobEventRepository,
) : StoreImpl<String, JobEventDomain, TravakoJobEvent>(
    travakoJobEventRepository
), JobEventStore {
    override fun JobEventDomain.map() = this as TravakoJobEvent

    override val storeQuery =
        JobEventStoreQueryImpl(travakoJobEventRepository)

    override fun identityCreator() =
        JobEventCreatorImpl()

    override fun JobEventDomain.identityUpdater() =
        JobEventUpdaterImpl(map())
}
