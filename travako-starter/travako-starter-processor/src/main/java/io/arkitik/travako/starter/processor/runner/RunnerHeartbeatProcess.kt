package io.arkitik.travako.starter.processor.runner

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.starter.processor.config.TravakoConfig
import io.arkitik.travako.starter.processor.logger.logger
import io.arkitik.travako.starter.processor.scheduler.fixedRateJob
import org.springframework.scheduling.TaskScheduler

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 31 2:48 PM, **Fri, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RunnerHeartbeatProcess(
    private val travakoConfig: TravakoConfig,
    private val schedulerRunnerSdk: SchedulerRunnerSdk,
    private val taskScheduler: TaskScheduler,
    private val transactionalExecutor: TransactionalExecutor,
) : Processor<SchedulerRunnerDomain> {
    private val logger = logger<RunnerHeartbeatProcess>()
    override val type = SchedulerRunnerDomain::class.java

    override fun process() {
        travakoConfig.heartbeat
            .fixedRateJob(taskScheduler) {
                transactionalExecutor.runOnTransaction {
                    schedulerRunnerSdk.runCatching {
                        logRunnerHeartbeat.runOperation(RunnerKeyDto(
                            travakoConfig.serverKey,
                            travakoConfig.runnerKey,
                        ))
                    }.onFailure {
                        it.printStackTrace()
                        logger.error("Error while logging heartbeat message for {} , error: ",
                            travakoConfig.runnerKey,
                            it.message)
                    }
                }
            }
    }
}
