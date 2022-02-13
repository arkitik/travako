package io.arkitik.travako.operation.job.errors

import io.arkitik.radix.develop.shared.error.ErrorResponse

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:32 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
enum class JobErrors(
    override val code: String,
    override val message: String,
) : ErrorResponse {
    JOB_ALREADY_REGISTERED("TRVK-JOB-1000", "Job already registered"),
    JOB_IS_NOT_REGISTERED("TRVK-JOB-1200", "Job is not registered"),
    ONE_OR_JOB_ARE_NOT_REGISTERED("TRVK-JOB-1400", "One or more jobs are not registered"),
}
