package io.arkitik.travako.starter.job.registry

import io.arkitik.travako.starter.job.registry.dto.TravakoJobBeanData
import io.arkitik.travako.starter.job.registry.dto.TravakoJobBeanDataBuilder
import io.arkitik.travako.starter.job.registry.dto.jobBuilder
import org.springframework.scheduling.Trigger

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 9:39 PM, 25/08/2024
 */
interface JobInstancesRegistry {
    fun registerJob(jobBeanData: TravakoJobBeanData)
    fun registerJob(jobBeanData: TravakoJobBeanDataBuilder.() -> Unit) =
        registerJob(jobBuilder(jobBeanData))

    fun jobRegistered(jobKey: String): Boolean

    fun unregisterJob(jobKey: String)

    fun updateJobParams(jobKey: String, params: Map<String, String?>)

    fun updateJobTrigger(jobKey: String, jobTrigger: Trigger)
}
