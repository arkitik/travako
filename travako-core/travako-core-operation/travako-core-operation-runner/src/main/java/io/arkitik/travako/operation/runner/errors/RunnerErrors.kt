package io.arkitik.travako.operation.runner.errors

import io.arkitik.radix.develop.shared.error.ErrorResponse

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:32 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
enum class RunnerErrors(
    override val code: String,
    override val message: String,
) : ErrorResponse {
    RUNNER_ALREADY_REGISTERED("TRVK-RUNNER-1000", "Runner already registered"),
    RUNNER_IS_NOT_REGISTERED("TRVK-RUNNER-1100", "Runner is not registered"),
    NO_REGISTERED_RUNNER("TRVK-RUNNER-1200", "There is no registered runner"),
}
