package io.arkitik.travako.starter.startup.processor.leader

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.leader.LeaderSdk
import io.arkitik.travako.sdk.leader.dto.LeaderRunnerDetails
import io.arkitik.travako.sdk.leader.dto.LeaderServerKeyDto
import io.arkitik.travako.starter.startup.processor.config.TravakoConfig
import io.arkitik.travako.starter.startup.processor.logger.logger
import io.arkitik.travako.starter.startup.processor.scheduler.fixedRateJob
import org.springframework.scheduling.TaskScheduler
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.absoluteValue

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 10:02 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class LeaderRecoveringProcessor(
    private val travakoConfig: TravakoConfig,
    private val leaderSdk: LeaderSdk,
    private val taskScheduler: TaskScheduler,
    private val transactionalExecutor: TransactionalExecutor,
) : Processor<LeaderDomain> {
    override val type = LeaderDomain::class.java
    private val logger = logger<LeaderRecoveringProcessor>()

    private val recoveringSeconds = travakoConfig.leaderSwitch
        .seconds
        .times(travakoConfig.recoveringMultiplier)
        .toLong()

    override fun process() {
        Duration.ofSeconds(recoveringSeconds)
            .fixedRateJob(taskScheduler) {
                transactionalExecutor.runOnTransaction {
                    if (shouldBeRecover()) {
                        logger.debug("Start leader recovering processor")
                        val response = leaderSdk.switchLeader.runOperation(LeaderServerKeyDto(travakoConfig.serverKey))
                        logger.debug(
                            "Leader has been recovered, {} will be the leader for next duration",
                            response.runnerKey
                        )
                    }
                }
            }
    }

    private fun shouldBeRecover(): Boolean {
        val leaderRunnerDetails = leaderSdk.currentLeader.runOperation(LeaderServerKeyDto(
            travakoConfig.serverKey
        ))
        return when {
            leaderRunnerDetails.noHeartbeat() -> true
            leaderRunnerDetails.heartbeatLessThanExpectedTime() -> true
            leaderRunnerDetails.isRunning.not() -> true
            else -> false
        }
    }

    private fun LeaderRunnerDetails.noHeartbeat() = lastHeartbeat == null

    private fun LeaderRunnerDetails.heartbeatLessThanExpectedTime() =
        lastHeartbeat?.let {
            Duration.between(
                LocalDateTime.now(),
                it
            ).seconds.absoluteValue > recoveringSeconds
        } ?: true
}
