package io.arkitik.travako.starter.startup.processor.errors

import io.arkitik.radix.develop.shared.error.ErrorResponse

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 31 12:29 AM, **Fri, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
enum class StartupErrors(
    override val code: String,
    override val message: String,
) : ErrorResponse {
    NO_REGISTERED_JOBS("TRVK-STARTUP-0000", "No Registered JOBS"),
    NO_REGISTERED_RUNNERS("TRVK-STARTUP-9999", "No Registered Runners"),
}
