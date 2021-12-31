package io.arkitik.travako.sample.jobs

import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.startup.processor.logger.logger
import org.springframework.scheduling.support.PeriodicTrigger

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 5:38 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class Job3 : JobInstanceBean {
    override val trigger = PeriodicTrigger(10_000)

    override val jobKey = "JOB-3"
    private val logger = logger<Job3>()

    override fun runJob() {
        logger.info("JOB Running {}", jobKey)
    }
}
