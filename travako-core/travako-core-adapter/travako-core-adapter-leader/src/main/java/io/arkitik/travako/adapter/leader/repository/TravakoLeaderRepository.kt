package io.arkitik.travako.adapter.leader.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.entity.leader.TravakoLeader
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.server.TravakoServer
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:52 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoLeaderRepository : RadixRepository<String, TravakoLeader> {
    fun findByServer(server: TravakoServer): TravakoLeader?
    fun existsByServer(server: TravakoServer): Boolean

    fun existsByServerAndRunnerAndLastModifiedDateBefore(
        server: TravakoServer,
        runner: TravakoSchedulerRunner,
        lastModifiedDate: LocalDateTime,
    ): Boolean
}
