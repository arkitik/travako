package io.arkitik.travako.starter.processor.runner

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.function.processor.PreProcessor
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.starter.processor.config.TravakoConfig
import org.slf4j.LoggerFactory
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 8:42 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SchedulerRunnerRegistrationProcess(
    private val travakoConfig: TravakoConfig,
    private val schedulerRunnerSdk: SchedulerRunnerSdk,
    private val applicationContext: ApplicationContext,
) : PreProcessor<SchedulerRunnerDomain> {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(SchedulerRunnerRegistrationProcess::class.java)!!
    }

    override val type = SchedulerRunnerDomain::class.java

    override fun process() {
        try {
            schedulerRunnerSdk.registerRunner.runOperation(travakoConfig.keyDto)
        } catch (exception: Exception) {
            LOGGER.error(
                "A runner with the same key and host has been registered previously for the [Server {}], [Runner {}], " +
                        "the application will stop working till you provide a unique runner-key",
                travakoConfig.serverKey,
                "${travakoConfig.keyDto.runnerKey}-${travakoConfig.keyDto.runnerHost}")
            LOGGER.error(
                "Error while registering the Scheduler-Runner: [Key: {}] [Error: {}]",
                "${travakoConfig.keyDto.runnerKey}-${travakoConfig.keyDto.runnerHost}",
                exception
            )
            SpringApplication.exit(applicationContext, ExitCodeGenerator {
                0
            })
        }
    }
}
