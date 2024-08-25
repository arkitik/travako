package io.arkitik.travako.port.shared.function

import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import org.slf4j.LoggerFactory
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 11:26 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class TravakoTransactionalExecutorImpl(
    private val transactionManager: PlatformTransactionManager,
) : TravakoTransactionalExecutor {
    companion object {
        private val logger = LoggerFactory.getLogger(TravakoTransactionalExecutorImpl::class.java)
    }

    override fun <T> runOnTransaction(function: () -> T): T? {
        val transactionTemplate = TransactionTemplate(transactionManager)
        return transactionTemplate.execute {
            try {
                function()
            } catch (e: Exception) {
                logger.error("IGNORED: Error while executing transactional command", e)
                it.setRollbackOnly()
                null
            }
        }
    }
}
