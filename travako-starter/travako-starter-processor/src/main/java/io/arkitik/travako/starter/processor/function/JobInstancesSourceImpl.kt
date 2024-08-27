package io.arkitik.travako.starter.processor.function

import io.arkitik.travako.starter.job.source.JobInstancesSource

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 7:28 PM, 14/07/2024
 */
internal class JobInstancesSourceImpl(
    private val sourceUnits: List<JobInstancesSource.SourceUnit>,
) : JobInstancesSource {
    override fun jobs() =
        sourceUnits.flatMap(JobInstancesSource.SourceUnit::jobs)
}
