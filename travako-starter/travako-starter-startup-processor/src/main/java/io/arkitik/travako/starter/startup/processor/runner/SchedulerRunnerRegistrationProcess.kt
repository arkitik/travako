package io.arkitik.travako.starter.startup.processor.runner

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.exception.UnprocessableEntityException
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.starter.startup.processor.config.TravakoConfig
import org.slf4j.LoggerFactory

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 8:42 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SchedulerRunnerRegistrationProcess(
    private val travakoConfig: TravakoConfig,
    private val schedulerRunnerSdk: SchedulerRunnerSdk,
    private val transactionalExecutor: TransactionalExecutor,
) : Processor<SchedulerRunnerDomain> {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(SchedulerRunnerRegistrationProcess::class.java)!!
    }

    override val type = SchedulerRunnerDomain::class.java

    override fun process() {
        transactionalExecutor.runOnTransaction {
            try {
                schedulerRunnerSdk.registerRunner.runOperation(
                    RunnerKeyDto(
                        serverKey = travakoConfig.serverKey,
                        runnerKey = travakoConfig.runnerKey
                    ))
            } catch (e: UnprocessableEntityException) {
                LOGGER.error("Error while registering the Scheduler-Runner: [Key: {}] [Error: {}]",
                    travakoConfig.runnerKey,
                    e.error)
            }
        }
    }
}
