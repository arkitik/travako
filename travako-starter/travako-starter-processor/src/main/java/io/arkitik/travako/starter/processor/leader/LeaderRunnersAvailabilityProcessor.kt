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
import io.arkitik.travako.sdk.runner.dto.RunnerDetails
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.sdk.runner.dto.RunnerServerKeyDto
import io.arkitik.travako.starter.processor.config.TravakoRunnerConfig
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import io.arkitik.travako.starter.processor.core.logger.logger
import io.arkitik.travako.starter.processor.errors.StartupErrors
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
    private val travakoRunnerConfig: TravakoRunnerConfig,
) : Processor<LeaderDomain> {
    companion object {
        private val logger = logger<LeaderRunnersAvailabilityProcessor>()
    }

    override val type = LeaderDomain::class.java

    private val lastHeartbeatSeconds = travakoRunnerConfig.heartbeat.seconds.times(2)

    override fun process() {
        Duration.ofSeconds(travakoRunnerConfig.heartbeat.seconds.times(2)).fixedRateJob(taskScheduler) {
            travakoTransactionalExecutor.runUnitTransaction {
                val runners = schedulerRunnerSdk.allServerRunners
                    .runOperation(RunnerServerKeyDto(travakoConfig.serverKey))
                if (runners.isEmpty()) {
                    throw StartupErrors.NO_REGISTERED_RUNNERS.internal()
                }
                runners.filter { runnerDetails ->
                    runnerDetails.isRunning || runnerDetails.isLeader
                }.filter { runnerDetails ->
                    !isCurrentRunner(runnerDetails)
                }.filter {
                    it.heartbeatLessThanExpectedTime(lastHeartbeatSeconds)
                }.forEach { runnerDetails ->
                    if (runnerDetails.isRunning) {
                        logger.debug(
                            "Runner {} marked as DOWN since no heartbeat message has been logged from {} seconds",
                            "${runnerDetails.runnerKey}-${runnerDetails.runnerHost}",
                            lastHeartbeatSeconds
                        )
                        schedulerRunnerSdk.markRunnerAsDown
                            .runOperation(
                                RunnerKeyDto(
                                    serverKey = travakoConfig.serverKey,
                                    runnerKey = runnerDetails.runnerKey,
                                    runnerHost = runnerDetails.runnerHost
                                )
                            )
                    }
                    if (runnerDetails.isLeader) {
                        logger.debug(
                            "Start leader recovering process since Runner {} was the selected leader",
                            "${runnerDetails.runnerKey}-${runnerDetails.runnerHost}"
                        )
                        logger.debug(
                            "Start moving {} leader processor responsibilities.",
                            travakoConfig.serverKey,
                        )
                        val leaderRunnerKeyDto = leaderSdk.switchLeader
                            .runOperation(LeaderServerKeyDto(travakoConfig.serverKey))
                        logger.debug(
                            "{} has been promoted to be {} leader for the next running period",
                            "${leaderRunnerKeyDto.runnerKey}-${leaderRunnerKeyDto.runnerHost}",
                            leaderRunnerKeyDto.serverKey,
                        )
                    }
                }
            }
        }
    }

    private fun isCurrentRunner(
        runnerDetails: RunnerDetails,
    ): Boolean {
        return runnerDetails.runnerKey == travakoRunnerConfig.key && runnerDetails.runnerHost == travakoRunnerConfig.host
    }
}
