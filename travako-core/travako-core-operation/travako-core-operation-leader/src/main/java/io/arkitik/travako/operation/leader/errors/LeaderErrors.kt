package io.arkitik.travako.operation.leader.errors

import io.arkitik.radix.develop.shared.error.ErrorResponse

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:32 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
enum class LeaderErrors(
    override val code: String,
    override val message: String,
) : ErrorResponse {
    SERVER_LEADER_ALREADY_REGISTERED("TRVK-LEADER-1000", "Server leader already registered"),
    SERVER_LEADER_NOT_REGISTERED("TRVK-LEADER-1200", "Server leader not yet registered"),
}
