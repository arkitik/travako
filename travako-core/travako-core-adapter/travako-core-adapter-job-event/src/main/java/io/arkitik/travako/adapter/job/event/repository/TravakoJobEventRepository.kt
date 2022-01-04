package io.arkitik.travako.adapter.job.event.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.entity.job.event.TravakoJobEvent
import io.arkitik.travako.entity.server.TravakoServer

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 4:50 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoJobEventRepository : RadixRepository<String, TravakoJobEvent> {
    fun findAllByJobInstanceServerAndProcessedFlagFalse(server: TravakoServer): List<TravakoJobEvent>
}
