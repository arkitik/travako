package io.arkitik.travako.starter.processor.leader

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.exception.UnprocessableEntityException
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.function.processor.PreProcessor
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.sdk.leader.LeaderSdk
import io.arkitik.travako.sdk.leader.dto.LeaderRunnerKeyDto
import io.arkitik.travako.starter.processor.config.TravakoRunnerConfig
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import io.arkitik.travako.starter.processor.core.logger.logger

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 8:42 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class LeaderRegistrationProcess(
    private val travakoConfig: TravakoConfig,
    private val leaderSdk: LeaderSdk,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
    private val travakoRunnerConfig: TravakoRunnerConfig,
) : PreProcessor<LeaderDomain> {
    companion object {
        private val logger = logger<LeaderRegistrationProcess>()
    }

    override val type = LeaderDomain::class.java

    override fun process() {
        travakoTransactionalExecutor.runUnitTransaction {
            logger.debug(
                "Start Registering Leader: [Key: {}]",
                travakoConfig.serverKey
            )
            try {
                leaderSdk.registerLeaderServer.runOperation(
                    LeaderRunnerKeyDto(
                        serverKey = travakoConfig.serverKey,
                        runnerKey = travakoRunnerConfig.key,
                        runnerHost = travakoRunnerConfig.host
                    )
                )
            } catch (e: UnprocessableEntityException) {
                logger.warn(
                    "Error while registering the Leader: [Server: {}] [Runner: {}] [Error: {}]",
                    travakoConfig.serverKey,
                    "${travakoRunnerConfig.key}-${travakoRunnerConfig.host}",
                    e.error
                )
            }
        }
    }
}
