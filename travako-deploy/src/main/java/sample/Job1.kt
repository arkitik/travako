package sample

import io.arkitik.travako.starter.job.bean.JobInstanceBean
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.support.PeriodicTrigger
import org.springframework.stereotype.Service
import java.time.Duration

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 11:50 PM, 19/08/2025
 */
@Service
class Job1 : JobInstanceBean {
    override val trigger: Trigger = PeriodicTrigger(Duration.ofSeconds(10))

    override fun runJob() {
        println("Job1 Running")
    }
}

@Service
class Job2 : JobInstanceBean {
    override val trigger: Trigger = PeriodicTrigger(Duration.ofSeconds(10))

    override fun runJob() {
        println("Job2 Running")
    }
}

@Service
class Job3 : JobInstanceBean {
    override val trigger: Trigger = PeriodicTrigger(Duration.ofSeconds(10))

    override fun runJob() {
        println("Job3 Running")
    }
}