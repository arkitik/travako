package io.arkitik.travako.starter.job.bean.dto

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:54 PM, 02/09/2025
 */
sealed interface TravakoJobExecutionResult {
    companion object {
        object Success : TravakoJobExecutionResult
        object Failure : TravakoJobExecutionResult

        fun success() = Success
        fun failure() = Failure
    }
}
