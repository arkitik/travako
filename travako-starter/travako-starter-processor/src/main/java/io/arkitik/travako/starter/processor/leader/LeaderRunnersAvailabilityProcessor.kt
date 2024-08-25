package io.arkitik.travako.starter.processor.leader

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.internal
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.sdk.leader.LeaderSdk
import io.arkitik.travako.sdk.leader.dto.LeaderServerKeyDto
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.sdk.runner.dto.RunnerServerKeyDto
import io.arkitik.travako.starter.processor.config.TravakoConfig
import io.arkitik.travako.starter.processor.errors.StartupErrors
import io.arkitik.travako.starter.processor.logger.logger
import io.arkitik.travako.starter.processor.runner.heartbeatLessThanExpectedTime
import io.arkitik.travako.starter.processor.scheduler.fixedRateJob
import org.springframework.scheduling.TaskScheduler
import java.time.Duration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 31 12:56 AM, **Fri, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class LeaderRunnersAvailabilityProcessor(
    private val travakoConfig: TravakoConfig,
    private val taskScheduler: TaskScheduler,
    private val schedulerRunnerSdk: SchedulerRunnerSdk,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
    private val leaderSdk: LeaderSdk,
) : Processor<LeaderDomain> {
    companion object {
        private val logger = logger<LeaderRunnersAvailabilityProcessor>()
    }

    override val type = LeaderDomain::class.java

    private val lastHeartbeatSeconds = travakoConfig.heartbeat.seconds.times(2)

    override fun process() {
        Duration.ofSeconds(travakoConfig.heartbeat.seconds.times(2))
            .fixedRateJob(taskScheduler) {
                travakoTransactionalExecutor.runUnitTransaction {
                    val runners = schedulerRunnerSdk.allServerRunners
                        .runOperation(RunnerServerKeyDto(travakoConfig.serverKey))
                    if (runners.isEmpty()) {
                        throw StartupErrors.NO_REGISTERED_RUNNERS.internal()
                    }
                    runners.filter {
                        it.isRunning || it.isLeader
                    }.filter {
                        !travakoConfig.isSelf(it.runnerKey, it.runnerHost)
                    }.filter {
                        it.heartbeatLessThanExpectedTime(lastHeartbeatSeconds)
                    }.forEach {
                        if (it.isRunning) {
                            logger.debug(
                                "Runner {} marked as DOWN since no heartbeat message has been logged from {} seconds",
                                "${it.runnerKey}-${it.runnerHost}",
                                lastHeartbeatSeconds
                            )
                            schedulerRunnerSdk.markRunnerAsDown
                                .runOperation(
                                    RunnerKeyDto(
                                        serverKey = travakoConfig.serverKey,
                                        runnerKey = it.runnerKey,
                                        runnerHost = it.runnerHost
                                    )
                                )
                        }
                        if (it.isLeader) {
                            logger.debug(
                                "Start leader recovering process since Runner {} was the selected leader",
                                "${it.runnerKey}-${it.runnerHost}"
                            )
                            logger.debug(
                                "Start moving {} leader processor responsibilities.",
                                travakoConfig.serverKey,
                            )
                            val response = leaderSdk.switchLeader
                                .runOperation(LeaderServerKeyDto(travakoConfig.serverKey))
                            logger.debug(
                                "{} has been promoted to be {} leader for the next running period",
                                "${response.runnerKey}-${response.runnerHost}",
                                response.serverKey,
                            )
                        }
                    }
                }
            }
    }
}
