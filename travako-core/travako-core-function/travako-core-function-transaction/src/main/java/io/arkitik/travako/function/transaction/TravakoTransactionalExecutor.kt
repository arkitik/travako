package io.arkitik.travako.function.transaction

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 11:21 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoTransactionalExecutor {
    fun <T> runOnTransaction(function: () -> T): T?
}

fun TravakoTransactionalExecutor.runUnitTransaction(function: () -> Unit) = runOnTransaction(function)
