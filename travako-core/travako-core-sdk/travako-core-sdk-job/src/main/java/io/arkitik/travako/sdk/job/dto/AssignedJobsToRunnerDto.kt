package io.arkitik.travako.sdk.job.dto

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 9:18 PM, 26/08/2024
 */
data class AssignedJobsToRunnerDto(
    val serverKey: String,
    val runnerKey: String,
    val runnerHost: String,
    val jobKeys: List<AssignedRunnerJobDto>,
)

data class AssignedRunnerJobDto(
    val jobKey: String,
    val jobClassName: String,
)
