package io.arkitik.travako.starter.job.source

import io.arkitik.travako.starter.job.bean.TravakoJob

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 7:10 PM, 14/07/2024
 */
interface JobInstancesSource {
    fun jobs(): List<TravakoJob>
    interface SourceUnit {
        fun jobs(): List<TravakoJob>
    }
}
