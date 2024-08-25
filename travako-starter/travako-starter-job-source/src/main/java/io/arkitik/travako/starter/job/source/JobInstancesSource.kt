package io.arkitik.travako.starter.job.source

import io.arkitik.travako.starter.job.bean.JobInstanceBean

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 7:10 PM, 14/07/2024
 */
interface JobInstancesSource : List<JobInstanceBean> {
    interface SourceUnit {
        fun jobs(): List<JobInstanceBean>
    }
}
