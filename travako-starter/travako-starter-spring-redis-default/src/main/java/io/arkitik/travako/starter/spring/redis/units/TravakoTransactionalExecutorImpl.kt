package io.arkitik.travako.starter.spring.redis.units

import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 11:26 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal object TravakoTransactionalExecutorImpl : TravakoTransactionalExecutor {
    override fun <T> runOnTransaction(function: () -> T): T? {
        return function()
    }
}
