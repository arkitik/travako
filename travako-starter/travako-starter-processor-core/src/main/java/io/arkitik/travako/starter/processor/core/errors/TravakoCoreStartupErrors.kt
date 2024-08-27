package io.arkitik.travako.starter.processor.core.errors

import io.arkitik.radix.develop.shared.error.ErrorResponse

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 31 12:29 AM, **Fri, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
enum class TravakoCoreStartupErrors(
    override val code: String,
    override val message: String,
) : ErrorResponse {
    JOB_CLASS_NOT_FOUND("TRAVAKO-CORE-STARTUP-0000", "Job Class not found"),
    NO_PROVIDER_TO_CREATE_JOB("TRAVAKO-CORE-STARTUP-1111", "No provider to create jobs"),
}
