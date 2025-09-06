package io.arkitik.travako.starter.job.registry.dto

import io.arkitik.travako.starter.job.bean.StatefulTravakoJob
import org.springframework.scheduling.Trigger
import java.time.Instant

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 8:58 PM, 26/08/2024
 */
class TravakoJobBeanData(
    val jobKey: String,
    val jobTrigger: Trigger,
    val jobClass: Class<out StatefulTravakoJob>,
    val params: Map<String, String?>,
    val firingTime: Instant?,
    val singleRun: Boolean = false,
)
