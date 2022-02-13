package io.arkitik.travako.operation.server.errors

import io.arkitik.radix.develop.shared.error.ErrorResponse

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:32 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
enum class ServerErrors(
    override val code: String,
    override val message: String,
) : ErrorResponse {
    SERVER_ALREADY_REGISTERED("TRVK-SERVER-1000", "Server already registered"),
    SERVER_NOT_REGISTERED("TRVK-SERVER-1200", "Server is not registered"),
}
