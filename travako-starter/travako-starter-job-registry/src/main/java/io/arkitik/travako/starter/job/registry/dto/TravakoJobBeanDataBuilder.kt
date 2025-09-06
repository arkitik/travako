package io.arkitik.travako.starter.job.registry.dto

import io.arkitik.travako.starter.job.bean.StatefulTravakoJob
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.support.CronTrigger
import org.springframework.scheduling.support.PeriodicTrigger
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import kotlin.reflect.KClass

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 8:53 PM, 26/08/2024
 */
class TravakoJobBeanDataBuilder {
    private lateinit var jobKey: String
    private lateinit var jobClass: Class<out StatefulTravakoJob>
    private lateinit var jobTrigger: Trigger
    private val params: MutableMap<String, String?> = mutableMapOf()

    private var firingTime: Instant? = null

    private var singleRun = false

    fun singleRun(singleRun: Boolean): TravakoJobBeanDataBuilder {
        this.singleRun = singleRun
        return this
    }

    fun jobKey(jobKey: String): TravakoJobBeanDataBuilder {
        this.jobKey = jobKey
        return this
    }

    fun firingTime(firingTime: LocalTime): TravakoJobBeanDataBuilder {
        return firingTime(firingTime.atDate(LocalDate.now()))
    }

    fun firingTime(firingTime: LocalDateTime): TravakoJobBeanDataBuilder {
        return firingTime(firingTime.atZone(ZoneId.systemDefault()).toInstant())
    }

    fun firingTime(firingTime: Instant): TravakoJobBeanDataBuilder {
        return zonedFiringTime(firingTime.atZone(ZoneId.systemDefault()).toInstant())
    }

    fun zonedFiringTime(firingTime: Instant): TravakoJobBeanDataBuilder {
        this.firingTime = firingTime
        return this
    }

    fun jobClass(jobClass: Class<out StatefulTravakoJob>): TravakoJobBeanDataBuilder {
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
            jobTrigger = jobTrigger,
            jobClass = jobClass,
            params = params,
            firingTime = firingTime,
            singleRun = singleRun,
        )
}


fun TravakoJobBeanDataBuilder.forever(): TravakoJobBeanDataBuilder {
    return singleRun(false)
}

fun TravakoJobBeanDataBuilder.oneTime(): TravakoJobBeanDataBuilder {
    return singleRun(true)
}

fun jobBuilder(builder: TravakoJobBeanDataBuilder.() -> Unit) =
    TravakoJobBeanDataBuilder().apply(builder).build()

fun KClass<out StatefulTravakoJob>.jobBuilder(builder: TravakoJobBeanDataBuilder.() -> Unit) =
    this.java.jobBuilder(builder)

fun Class<out StatefulTravakoJob>.jobBuilder(builder: TravakoJobBeanDataBuilder.() -> Unit) =
    TravakoJobBeanDataBuilder().jobClass(this).apply(builder).build()
