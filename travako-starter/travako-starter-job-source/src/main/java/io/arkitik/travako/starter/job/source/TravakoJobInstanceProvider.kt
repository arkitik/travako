package io.arkitik.travako.starter.job.source

import io.arkitik.travako.starter.job.bean.StatefulTravakoJob

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 12:50 PM, 27/08/2024
 */
fun interface TravakoJobInstanceProvider {
    fun provideJobInstance(jobKey: String, jobClassName: String): StatefulTravakoJob

    interface ProviderUnit {
        fun isSupported(jobKey: String, jobClassName: String): Boolean
        fun provideJobInstance(jobKey: String, jobClassName: String): StatefulTravakoJob
    }
}
