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
import java.util.concurrent.TimeUnit

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
            when (travakoConfig.duplicationProcessor) {
                true -> startDuplicationProcessor(exception)
                false -> stopServer(exception)
            }
        }
    }

    private fun stopServer(exception: Exception) {
        LOGGER.error(
            "A runner with the same key and host has been registered previously for the server {}, runner-key {}, " +
                    "the application will stop working till you provide a unique runner-key",
            travakoConfig.serverKey,
            travakoConfig.runnerKey
        )
        LOGGER.error(
            "Error while registering the Scheduler-Runner: [Key: {}] [Error: {}]",
            travakoConfig.runnerKey,
            exception.message,
        )
        SpringApplication.exit(applicationContext, ExitCodeGenerator {
            0
        })
    }

    private fun startDuplicationProcessor(exception: Exception) {
        LOGGER.info(
            "Duplication processor marked as required, server will start the internal process to validate such duplication",
        )
        LOGGER.warn(
            "It's danger to this configuration enabled, this feature added for the sudden cases, so; It is better to find tho root cause of requiring such",
        )
        LOGGER.warn(
            "A runner with the same key and host has been registered previously for the [Server {}], [Runner {}], " +
                    "The server will try to check the other instance state.",
            travakoConfig.serverKey,
            "${travakoConfig.keyDto.runnerKey}-${travakoConfig.keyDto.runnerHost}"
        )

        TimeUnit.SECONDS.runCatching {
            LOGGER.warn(
                "Server detecting the duplicated instance states for [Server {}], [Runner {}]",
                travakoConfig.serverKey,
                "${travakoConfig.keyDto.runnerKey}-${travakoConfig.keyDto.runnerHost}"
            )
            LOGGER.warn(
                "Runner uniqueness processor started for [Server {}], [Runner {}]",
                travakoConfig.serverKey,
                "${travakoConfig.keyDto.runnerKey}-${travakoConfig.keyDto.runnerHost}"
            )
            sleep(travakoConfig.heartbeat.seconds.times(2))
        }.onSuccess {
            val runnerDetails = schedulerRunnerSdk.runnerDetails.runOperation(travakoConfig.keyDto)
            if (runnerDetails.heartbeatLessThanExpectedTime(travakoConfig.heartbeat.seconds.times(2))) {
                LOGGER.warn(
                    "Server detected unhealthy state from the other instance [Server {}], [Runner {}]",
                    travakoConfig.serverKey,
                    "${travakoConfig.keyDto.runnerKey}-${travakoConfig.keyDto.runnerHost}"
                )
                LOGGER.warn(
                    "The instance will be registered as RUNNING state for [Server {}], [Runner {}]",
                    travakoConfig.serverKey,
                    "${travakoConfig.keyDto.runnerKey}-${travakoConfig.keyDto.runnerHost}"
                )
            } else {
                LOGGER.error(
                    "Error while registering the Scheduler-Runner: [Key: {}] [Error: {}], the application stop working till a unique runner-key provided",
                    "${travakoConfig.keyDto.runnerKey}-${travakoConfig.keyDto.runnerHost}",
                    exception.message,
                    exception,
                )
                SpringApplication.exit(applicationContext, ExitCodeGenerator {
                    0
                })
            }
        }.onFailure {
            Thread.currentThread().interrupt()
            LOGGER.error(
                "Error while registering the Scheduler-Runner: [Key: {}] [Error: {}], the application stop working till a unique runner-key provided",
                "${travakoConfig.keyDto.runnerKey}-${travakoConfig.keyDto.runnerHost}",
                exception.message,
                exception,
            )
            SpringApplication.exit(applicationContext, ExitCodeGenerator {
                0
            })
        }
    }
}
