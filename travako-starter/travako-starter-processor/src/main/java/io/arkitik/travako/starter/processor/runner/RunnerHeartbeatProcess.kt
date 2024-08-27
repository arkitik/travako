package io.arkitik.travako.starter.processor.runner

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.starter.processor.config.TravakoRunnerConfig
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import io.arkitik.travako.starter.processor.core.logger.logger
import io.arkitik.travako.starter.processor.scheduler.fixedRateJob
import org.springframework.scheduling.TaskScheduler

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 31 2:48 PM, **Fri, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class RunnerHeartbeatProcess(
    private val travakoConfig: TravakoConfig,
    private val travakoRunnerConfig: TravakoRunnerConfig,
    private val schedulerRunnerSdk: SchedulerRunnerSdk,
    private val taskScheduler: TaskScheduler,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
) : Processor<SchedulerRunnerDomain> {
    companion object {
        private val logger = logger<RunnerHeartbeatProcess>()
    }

    override val type = SchedulerRunnerDomain::class.java

    override fun process() {
        travakoRunnerConfig.heartbeat
            .fixedRateJob(taskScheduler) {
                travakoTransactionalExecutor.runUnitTransaction {
                    schedulerRunnerSdk.runCatching {
                        logRunnerHeartbeat.runOperation(
                            RunnerKeyDto(
                                serverKey = travakoConfig.serverKey,
                                runnerKey = travakoRunnerConfig.key,
                                runnerHost = travakoRunnerConfig.host
                            )
                        )
                    }.onFailure {
                        logger.warn(
                            "Error while logging heartbeat message for {} , error: {}",
                            "${travakoRunnerConfig.key}-${travakoRunnerConfig.host}",
                            it.message
                        )
                    }
                }
            }
    }
}
