package io.arkitik.travako.deploy.sample

import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.job.bean.StatefulTravakoJob
import io.arkitik.travako.starter.job.bean.dto.TravakoJobExecutionData
import io.arkitik.travako.starter.job.bean.dto.TravakoJobExecutionResult
import io.arkitik.travako.starter.job.registry.JobInstancesRegistry
import io.arkitik.travako.starter.job.registry.dto.jobBuilder
import io.arkitik.travako.starter.job.registry.dto.oneTime
import org.springframework.boot.CommandLineRunner
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.support.PeriodicTrigger
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalTime
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement

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

@OptIn(ExperimentalAtomicApi::class)
private val atomicLong = AtomicLong(0)

@Service
class Job4 : StatefulTravakoJob {
    @OptIn(ExperimentalAtomicApi::class)
    override fun executeJob(executionData: TravakoJobExecutionData): TravakoJobExecutionResult {
        val fetchAndIncrement = atomicLong.fetchAndIncrement()
        println("Job4 Running: $fetchAndIncrement, ${executionData.jobKey}, ${executionData.runnerKey}, ${executionData.runnerHost}, ${executionData.serverKey}, ${executionData.params}")
        if (fetchAndIncrement == 10L) {
            return TravakoJobExecutionResult.success()
        }
        if (fetchAndIncrement == 5L) {
            throw RuntimeException("Job4 Failed")
        }
        return TravakoJobExecutionResult.failure()
    }
}

@Service
class SampleJobRegistry(
    private val jobInstancesRegistry: JobInstancesRegistry,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        if (!jobInstancesRegistry.jobRegistered("sample")) {
            jobInstancesRegistry.registerJob(Job4::class.jobBuilder {
                jobKey("sample")
                oneTime()
                addParam("sample-param", "sample-param")
                cronTrigger(LocalTime.now().minusSeconds(10))
            })
        }
        if (!jobInstancesRegistry.jobRegistered("sample-1")) {
            jobInstancesRegistry.registerJob(Job4::class.jobBuilder {
                jobKey("sample-1")
                oneTime()
                addParam("sample-param", "sample-param-1")
                cronTrigger(LocalTime.now().minusSeconds(10))
            })
        }
        if (!jobInstancesRegistry.jobRegistered("sample-2")) {
            jobInstancesRegistry.registerJob(Job4::class.jobBuilder {
                jobKey("sample-2")
                oneTime()
                addParam("sample-param", "sample-param-2")
                cronTrigger(LocalTime.now().minusSeconds(10))
            })
        }
        if (!jobInstancesRegistry.jobRegistered("sample-3")) {
            jobInstancesRegistry.registerJob(Job4::class.jobBuilder {
                jobKey("sample-3")
                oneTime()
                addParam("sample-param", "sample-param-3")
                cronTrigger(LocalTime.now().minusSeconds(10))
            })
        }
    }

}