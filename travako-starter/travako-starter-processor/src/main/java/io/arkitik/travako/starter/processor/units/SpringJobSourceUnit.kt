package io.arkitik.travako.starter.processor.units

import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.job.bean.StatefulTravakoJob
import io.arkitik.travako.starter.job.source.JobInstancesSource

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 7:30 PM, 14/07/2024
 */
internal class SpringJobSourceUnit(
    private val jobInstanceBeans: List<JobInstanceBean>,
) : JobInstancesSource.SourceUnit {
    override fun jobs(): List<StatefulTravakoJob> = jobInstanceBeans
}
