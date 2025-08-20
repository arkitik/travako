package io.arkitik.travako.starter.spring.exposed.custom.units

import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 11:26 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class TravakoTransactionalExecutorImpl(
    private val database: Database,
) : TravakoTransactionalExecutor {
    override fun <T> runOnTransaction(function: () -> T): T? {
        return transaction(database) {
            function()
        }
    }
}
