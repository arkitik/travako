package io.arkitik.travako.starter.processor.function

import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.job.source.JobInstancesSource

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 7:28 PM, 14/07/2024
 */
internal class JobInstancesSourceImpl(
    sourceUnits: List<JobInstancesSource.SourceUnit>,
) : JobInstancesSource, List<JobInstanceBean> by sourceUnits.flatMap(JobInstancesSource.SourceUnit::jobs)
