package io.arkitik.travako.starter.processor.server

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.exception.UnprocessableEntityException
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.function.processor.PreProcessor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.server.ServerSdk
import io.arkitik.travako.sdk.server.dto.ServerKeyDto
import io.arkitik.travako.starter.processor.config.TravakoConfig
import org.slf4j.LoggerFactory

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 8:42 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ServerRegistrationProcess(
    private val travakoConfig: TravakoConfig,
    private val serverSdk: ServerSdk,
    private val transactionalExecutor: TransactionalExecutor,
) : PreProcessor<ServerDomain> {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(ServerRegistrationProcess::class.java)!!
    }

    override val type = ServerDomain::class.java

    override fun process() {
        transactionalExecutor.runOnTransaction<Unit> {
            try {
                val serverKeyDto = ServerKeyDto(travakoConfig.serverKey)
                serverSdk.isServerRegistered.operateRole(serverKeyDto)
                    .takeIf { !it }?.also {
                        serverSdk.registerServer.runOperation(serverKeyDto)
                    }
            } catch (e: UnprocessableEntityException) {
                LOGGER.warn(
                    "Error while registering the Server: [Key: {}] [Error: {}]",
                    travakoConfig.serverKey,
                    e.error
                )
            }
        }
    }
}
