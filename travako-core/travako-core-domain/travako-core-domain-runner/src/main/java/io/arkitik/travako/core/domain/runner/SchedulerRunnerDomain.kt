package io.arkitik.travako.core.domain.runner

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 4:39 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 *
 * =================================================
 * Scheduler instance will responsible to:
 * * Non-Leader Instance will responsible to run all jobs unless there is no instance expect one, then; the same instance will run all scheduled jobs.
 */
interface SchedulerRunnerDomain : Identity<String> {
    override val uuid: String
    val server: ServerDomain
    val runnerKey: String
    val instanceState: InstanceState
    val lastHeartbeatTime: LocalDateTime?
}
