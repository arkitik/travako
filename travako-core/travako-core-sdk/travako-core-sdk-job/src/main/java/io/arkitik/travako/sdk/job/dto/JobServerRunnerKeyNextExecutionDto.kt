package io.arkitik.travako.sdk.job.dto

import java.time.LocalDateTime

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 6:11 PM, 26/08/2024
 */
class JobServerRunnerKeyNextExecutionDto(
    val serverKey: String,
    val runnerKey: String,
    val runnerHost: String,
    val executionTime: LocalDateTime,
)
