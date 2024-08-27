package io.arkitik.travako.starter.job.source

import io.arkitik.travako.starter.job.bean.JobInstanceBean

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 7:10 PM, 14/07/2024
 */
@Deprecated(
    message = "Will be removed in future releases, jobs now can be registered using either initial sql scripts or dynamically using io.arkitik.travako.starter.job.registry.JobInstancesRegistry.",
)
interface JobInstancesSource {
    fun jobs(): List<JobInstanceBean>
    interface SourceUnit {
        fun jobs(): List<JobInstanceBean>
    }
}
