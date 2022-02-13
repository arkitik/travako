package io.arkitik.travako.store.job.event

import io.arkitik.radix.develop.store.Store
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.store.job.event.creator.JobEventCreator
import io.arkitik.travako.store.job.event.query.JobEventStoreQuery
import io.arkitik.travako.store.job.event.updater.JobEventUpdater

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:10 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobEventStore : Store<String, JobEventDomain> {
    override val storeQuery: JobEventStoreQuery

    override fun identityCreator(): JobEventCreator

    override fun JobEventDomain.identityUpdater(): JobEventUpdater
}
