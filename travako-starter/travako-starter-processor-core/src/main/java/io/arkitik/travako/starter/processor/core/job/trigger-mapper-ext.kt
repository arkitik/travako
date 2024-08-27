package io.arkitik.travako.starter.processor.core.job

import io.arkitik.radix.develop.shared.error.RadixError
import io.arkitik.radix.develop.shared.ext.internal
import io.arkitik.travako.sdk.job.dto.JobDetails
import org.springframework.boot.convert.DurationStyle
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.support.CronTrigger
import org.springframework.scheduling.support.PeriodicTrigger
import org.springframework.scheduling.support.SimpleTriggerContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 05 5:16 PM, **Sat, February 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
private val timeUnitsMapper = hashMapOf<TimeUnit, String>().apply {
    this[TimeUnit.DAYS] = "d"
    this[TimeUnit.HOURS] = "h"
    this[TimeUnit.MINUTES] = "m"
    this[TimeUnit.SECONDS] = "s"
    this[TimeUnit.MILLISECONDS] = "ms"
}

fun Trigger.parseTrigger(): Pair<String, Boolean> {
    return when (this) {
        is CronTrigger -> {
            expression to false
        }

        is PeriodicTrigger -> {
            "${periodDuration.toMillis()}${timeUnitsMapper[TimeUnit.MILLISECONDS]}" to true
        }

        else -> {
            throw RadixError(
                "INTERNAL-ERROR",
                "trigger is not supported $this"
            ).internal()
        }
    }
}

fun Trigger.nextTimeToExecution(instant: Instant? = Instant.now()): LocalDateTime? {
    val currentInstant = instant ?: Instant.now()
    return when (this) {
        is CronTrigger -> {
            nextExecution(
                SimpleTriggerContext(
                    currentInstant,
                    currentInstant,
                    currentInstant,
                )
            )?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
        }

        is PeriodicTrigger -> {
            nextExecution(
                SimpleTriggerContext(
                    currentInstant,
                    currentInstant,
                    currentInstant,
                )
            ).atZone(ZoneId.systemDefault())?.toLocalDateTime()
        }

        else -> null
    }
}

fun JobDetails.asTrigger() =
    when (isDuration) {
        true -> PeriodicTrigger(DurationStyle.detect(jobTrigger).parse(jobTrigger))
        false -> CronTrigger(jobTrigger)
    }
