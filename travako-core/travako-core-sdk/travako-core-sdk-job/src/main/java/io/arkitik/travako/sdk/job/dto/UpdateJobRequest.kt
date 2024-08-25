package io.arkitik.travako.sdk.job.dto

import java.time.LocalDateTime

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 8:05 AM, 21/08/2024
 */
class UpdateJobRequest(
    val jobKey: JobKeyDto,
    val nextExecutionTime: LocalDateTime?,
)
