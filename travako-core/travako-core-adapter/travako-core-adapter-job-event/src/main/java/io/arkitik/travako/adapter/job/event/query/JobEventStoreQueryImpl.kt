package io.arkitik.travako.adapter.job.event.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.job.event.repository.TravakoJobEventRepository
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.entity.job.event.TravakoJobEvent
import io.arkitik.travako.entity.server.TravakoServer
import io.arkitik.travako.store.job.event.query.JobEventStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 4:51 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobEventStoreQueryImpl(
    private val travakoJobEventRepository: TravakoJobEventRepository,
) : StoreQueryImpl<String, JobEventDomain, TravakoJobEvent>(
    travakoJobEventRepository
), JobEventStoreQuery {
    override fun findAllPendingEventsForServer(server: ServerDomain) =
        travakoJobEventRepository.findAllByJobInstanceServerAndProcessedFlagFalseOrderByCreationDateAsc(server as TravakoServer)
}
