package io.arkitik.travako.operation.job.event.errors

import io.arkitik.radix.develop.shared.error.ErrorResponse

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:32 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
enum class JobEventErrors(
    override val code: String,
    override val message: String,
) : ErrorResponse {
    JOB_EVENT_IS_NOT_REGISTERED("TRVK-JOB-EVENT-1000", "Job event doesn't exists"),
}
