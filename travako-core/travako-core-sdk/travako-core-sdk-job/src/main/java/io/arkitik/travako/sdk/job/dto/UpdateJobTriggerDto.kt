package io.arkitik.travako.sdk.job.dto

import java.time.LocalDateTime

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 9:18 PM, 26/08/2024
 */
data class UpdateJobTriggerDto(
    val serverKey: String,
    val jobKey: String,
    val jobTrigger: String,
    val isDuration: Boolean,
    val nextExecution: LocalDateTime?,
)
