package io.arkitik.travako.protocol.job.dto

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:54 PM, 02/09/2025
 */
sealed interface TravakoJobExecutionResult {
    companion object {
        object Success : TravakoJobExecutionResult
        class Failure(val throwable: Throwable? = null) : TravakoJobExecutionResult

        fun success() = Success
        fun failure(throwable: Throwable? = null) = Failure(throwable)
    }
}
