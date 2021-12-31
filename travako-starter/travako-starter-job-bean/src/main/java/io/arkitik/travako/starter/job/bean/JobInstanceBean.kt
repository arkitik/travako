package io.arkitik.travako.starter.job.bean

import org.springframework.scheduling.Trigger

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 10:00 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobInstanceBean {
    val jobKey: String
    val trigger: Trigger

    fun runJob()
}
