package io.arkitik.travako.starter.processor.leader

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.exception.UnprocessableEntityException
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.leader.LeaderSdk
import io.arkitik.travako.sdk.leader.dto.LeaderRunnerKeyDto
import io.arkitik.travako.starter.processor.config.TravakoConfig
import org.slf4j.LoggerFactory

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 8:42 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class LeaderRegistrationProcess(
    private val travakoConfig: TravakoConfig,
    private val leaderSdk: LeaderSdk,
    private val transactionalExecutor: TransactionalExecutor,
) : Processor<LeaderDomain> {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(LeaderRegistrationProcess::class.java)!!
    }

    override val type = LeaderDomain::class.java

    override fun process() {
        transactionalExecutor.runOnTransaction {
            LOGGER.error(
                "Start Registering Leader: [Key: {}]",
                travakoConfig.serverKey
            )
            try {
                leaderSdk.registerLeaderServer.runOperation(
                    LeaderRunnerKeyDto(
                        travakoConfig.serverKey,
                        travakoConfig.runnerKey
                    ))
            } catch (e: UnprocessableEntityException) {
                LOGGER.error(
                    "Error while registering the Leader: [Server: {}] [Runner: {}] [Error: {}]",
                    travakoConfig.serverKey,
                    travakoConfig.runnerKey,
                    e.error
                )
            }
        }
    }
}
