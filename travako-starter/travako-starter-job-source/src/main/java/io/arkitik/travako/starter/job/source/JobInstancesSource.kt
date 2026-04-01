package io.arkitik.travako.starter.job.source

import io.arkitik.travako.protocol.job.StatefulTravakoJob

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 7:10 PM, 14/07/2024
 */
interface JobInstancesSource {
    fun jobs(): List<StatefulTravakoJob>
    interface SourceUnit {
        fun jobs(): List<StatefulTravakoJob>
    }
}
