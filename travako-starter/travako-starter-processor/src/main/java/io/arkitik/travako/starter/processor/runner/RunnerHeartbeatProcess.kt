package io.arkitik.travako.starter.processor.runner

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.starter.processor.config.TravakoConfig
import io.arkitik.travako.starter.processor.logger.logger
import io.arkitik.travako.starter.processor.scheduler.fixedRateJob
import org.springframework.scheduling.TaskScheduler

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 31 2:48 PM, **Fri, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class RunnerHeartbeatProcess(
    private val travakoConfig: TravakoConfig,
    private val schedulerRunnerSdk: SchedulerRunnerSdk,
    private val taskScheduler: TaskScheduler,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
) : Processor<SchedulerRunnerDomain> {
    private val logger = logger<RunnerHeartbeatProcess>()
    override val type = SchedulerRunnerDomain::class.java

    override fun process() {
        travakoConfig.heartbeat
            .fixedRateJob(taskScheduler) {
                travakoTransactionalExecutor.runUnitTransaction {
                    schedulerRunnerSdk.runCatching {
                        logRunnerHeartbeat.runOperation(travakoConfig.keyDto)
                    }.onFailure {
                        logger.warn(
                            "Error while logging heartbeat message for {} , error: {}",
                            "${travakoConfig.keyDto.runnerKey}-${travakoConfig.keyDto.runnerHost}",
                            it.message
                        )
                    }
                }
            }
    }
}
