package io.arkitik.travako.store.job.event.query

import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.domain.job.event.JobEventDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:14 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobEventStoreQuery : StoreQuery<String, JobEventDomain> {
    fun findAllPendingEventsForServer(
        server: ServerDomain,
    ): List<JobEventDomain>
}
