package io.arkitik.travako.sdk.job.dto

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 11:12 AM, 27/08/2024
 */
class UpdateJobParamsDto(
    val serverKey: String,
    val jobKey: String,
    val newParams: Map<String, String?>,
)
