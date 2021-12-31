package io.arkitik.travako.core.domain.leader

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 4:30 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 *
 * ========================================
 * Leader is responsible for following:
 * * Decide who is the next leader.
 * * Task distributor
 */
interface LeaderDomain : Identity<String> {
    override val uuid: String
    val server: ServerDomain
    val runner: SchedulerRunnerDomain
    val lastModifiedDate: LocalDateTime
}
