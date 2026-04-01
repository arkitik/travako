package io.arkitik.travako.protocol.job.dto

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 8:53 PM, 26/08/2024
 */
data class TravakoJobExecutionData(
    val serverKey: String,
    val runnerKey: String,
    val runnerHost: String,
    val jobKey: String,
    val params: Map<String, String?>,
)
