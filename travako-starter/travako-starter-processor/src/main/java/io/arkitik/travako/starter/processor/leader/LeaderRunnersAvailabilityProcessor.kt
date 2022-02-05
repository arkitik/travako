package io.arkitik.travako.starter.processor.leader

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.internal
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.leader.LeaderSdk
import io.arkitik.travako.sdk.leader.dto.LeaderServerKeyDto
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.sdk.runner.dto.RunnerDetails
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.sdk.runner.dto.RunnerServerKeyDto
import io.arkitik.travako.starter.processor.config.TravakoConfig
import io.arkitik.travako.starter.processor.errors.StartupErrors
import io.arkitik.travako.starter.processor.logger.logger
import io.arkitik.travako.starter.processor.scheduler.fixedRateJob
import org.springframework.scheduling.TaskScheduler
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.absoluteValue

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 31 12:56 AM, **Fri, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class LeaderRunnersAvailabilityProcessor(
    private val travakoConfig: TravakoConfig,
    private val taskScheduler: TaskScheduler,
    private val schedulerRunnerSdk: SchedulerRunnerSdk,
    private val transactionalExecutor: TransactionalExecutor,
    private val leaderSdk: LeaderSdk,
) : Processor<LeaderDomain> {
    override val type = LeaderDomain::class.java

    private val logger = logger<LeaderRunnersAvailabilityProcessor>()

    private val lastHeartbeatSeconds = travakoConfig.heartbeat.seconds.times(2)

    override fun process() {
        Duration.ofSeconds(travakoConfig.heartbeat.seconds.times(2))
            .fixedRateJob(taskScheduler) {
                transactionalExecutor.runOnTransaction {
                    val runners = schedulerRunnerSdk.allServerRunners
                        .runOperation(RunnerServerKeyDto(travakoConfig.serverKey))
                    if (runners.isEmpty()) {
                        throw StartupErrors.NO_REGISTERED_RUNNERS.internal()
                    }
                    runners.filter {
                        it.isRunning
                    }.filter {
                        it.runnerKey != travakoConfig.runnerKey
                    }.filter {
                        it.heartbeatLessThanExpectedTime()
                    }.forEach {
                        logger.debug("Runner {} marked as DOWN since no heartbeat message has been logged from {} seconds",
                            it.runnerKey,
                            lastHeartbeatSeconds)
                        schedulerRunnerSdk.markRunnerAsDown
                            .runOperation(RunnerKeyDto(
                                serverKey = travakoConfig.serverKey,
                                runnerKey = it.runnerKey
                            ))
                        if (it.isLeader) {
                            logger.debug(
                                "Start leader recovering process since Runner {} was marked as leader",
                                it.runnerKey
                            )
                            logger.debug(
                                "Start moving {} leader processor responsibilities.",
                                travakoConfig.serverKey,
                            )
                            val response =
                                leaderSdk.switchLeader.runOperation(LeaderServerKeyDto(travakoConfig.serverKey))
                            logger.debug(
                                "{} has been promoted to be {} leader for the next running period",
                                response.runnerKey,
                                response.serverKey,
                            )
                        }
                    }
                }
            }
    }

    private fun RunnerDetails.heartbeatLessThanExpectedTime() =
        lastHeartbeatTime?.let {
            Duration.between(
                LocalDateTime.now(),
                it
            ).seconds.absoluteValue > lastHeartbeatSeconds
        } ?: true
}
