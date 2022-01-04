package io.arkitik.travako.sample.jobs

import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.processor.logger.logger
import org.springframework.scheduling.support.CronTrigger

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 5:38 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class Job5 : JobInstanceBean {
    override val trigger = CronTrigger("*/10 * * * * *")
    override val jobKey = "JOB-5"
    private val logger = logger<Job5>()

    override fun runJob() {
        logger.info("JOB Running {}", jobKey)
    }
}
