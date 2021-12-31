package io.arkitik.travako.adapter.runner.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import org.springframework.data.jpa.repository.Lock
import javax.persistence.LockModeType

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:17 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoSchedulerRunnerRepository : RadixRepository<String, TravakoSchedulerRunner> {
    fun findByRunnerKeyAndServerServerKey(
        runnerKey: String,
        serverKey: String,
    ): TravakoSchedulerRunner?

    fun existsByRunnerKeyAndServerServerKey(runnerKey: String, serverKey: String): Boolean
    fun existsByRunnerKeyAndServerServerKeyAndInstanceState(
        runnerKey: String,
        serverKey: String,
        instanceState: InstanceState,
    ): Boolean

    @Lock(
        value = LockModeType.PESSIMISTIC_WRITE
    )
    fun findTopByServerServerKeyAndInstanceStateOrderByLastHeartbeatTimeAsc(
        serverKey: String,
        instanceState: InstanceState,
    ): TravakoSchedulerRunner?

    @Lock(
        value = LockModeType.PESSIMISTIC_WRITE
    )
    fun findAllByServerServerKey(serverKey: String): List<TravakoSchedulerRunner>
}
