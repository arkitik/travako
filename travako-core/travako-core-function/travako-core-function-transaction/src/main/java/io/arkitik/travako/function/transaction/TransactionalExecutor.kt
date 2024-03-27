package io.arkitik.travako.function.transaction

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 11:21 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TransactionalExecutor {
    fun <T> runOnTransaction(function: () -> T): T?
}

fun TransactionalExecutor.runUnitTransaction(function: () -> Unit) = runOnTransaction(function)
