package io.arkitik.travako.starter.job.registry.dto

import io.arkitik.travako.starter.job.bean.TravakoJob
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.support.CronTrigger
import org.springframework.scheduling.support.PeriodicTrigger
import java.time.Duration
import java.time.LocalTime

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 8:53 PM, 26/08/2024
 */
class TravakoJobBeanDataBuilder {
    private lateinit var jobKey: String
    private lateinit var jobClass: Class<out TravakoJob>
    private lateinit var jobTrigger: Trigger
    private val params: MutableMap<String, String?> = mutableMapOf()

    fun jobKey(jobKey: String): TravakoJobBeanDataBuilder {
        this.jobKey = jobKey
        return this
    }

    fun jobClass(jobClass: Class<out TravakoJob>): TravakoJobBeanDataBuilder {
        this.jobClass = jobClass
        return this
    }

    fun jobTrigger(jobTrigger: Trigger): TravakoJobBeanDataBuilder {
        this.jobTrigger = jobTrigger
        return this
    }

    fun cronTrigger(expression: String): TravakoJobBeanDataBuilder {
        this.jobTrigger = CronTrigger(expression)
        return this
    }

    fun cronTrigger(localTime: LocalTime): TravakoJobBeanDataBuilder {
        this.jobTrigger = CronTrigger(String.format("0 %s %s 1/1 * *", localTime.minute, localTime.hour))
        return this
    }

    fun periodicTrigger(duration: Duration): TravakoJobBeanDataBuilder {
        this.jobTrigger = PeriodicTrigger(duration)
        return this
    }

    fun secondsTrigger(rate: Long): TravakoJobBeanDataBuilder {
        this.jobTrigger = PeriodicTrigger(Duration.ofSeconds(rate))
        return this
    }

    fun minutesTrigger(rate: Long): TravakoJobBeanDataBuilder {
        this.jobTrigger = PeriodicTrigger(Duration.ofMinutes(rate))
        return this
    }

    fun hourlyTrigger(rate: Long): TravakoJobBeanDataBuilder {
        this.jobTrigger = PeriodicTrigger(Duration.ofHours(rate))
        return this
    }

    fun dailyTrigger(rate: Long): TravakoJobBeanDataBuilder {
        this.jobTrigger = PeriodicTrigger(Duration.ofDays(rate))
        return this
    }

    fun params(params: Map<String, String?>): TravakoJobBeanDataBuilder {
        this.params.clear()
        this.params.putAll(params)
        return this
    }

    fun addParam(key: String, value: String?): TravakoJobBeanDataBuilder {
        this.params[key] = value
        return this
    }

    fun addParams(params: Map<String, String?>): TravakoJobBeanDataBuilder {
        this.params.putAll(params)
        return this
    }

    fun build() =
        TravakoJobBeanData(
            jobKey = jobKey,
            jobClass = jobClass,
            params = params,
            jobTrigger = jobTrigger,
        )
}

fun jobBuilder(builder: TravakoJobBeanDataBuilder.() -> Unit) =
    TravakoJobBeanDataBuilder().apply(builder).build()

fun Class<out TravakoJob>.jobBuilder(builder: TravakoJobBeanDataBuilder.() -> Unit) =
    TravakoJobBeanDataBuilder().jobClass(this).apply(builder).build()
