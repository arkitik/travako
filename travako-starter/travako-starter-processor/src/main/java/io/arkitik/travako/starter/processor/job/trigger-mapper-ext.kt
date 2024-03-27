package io.arkitik.travako.starter.processor.job

import io.arkitik.radix.develop.shared.error.RadixError
import io.arkitik.radix.develop.shared.ext.internal
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.support.CronTrigger
import org.springframework.scheduling.support.PeriodicTrigger
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
            "${timeUnit.convert(period, TimeUnit.MILLISECONDS)}${timeUnitsMapper[timeUnit]}" to true
        }

        else -> {
            throw RadixError(
                "INTERNAL-ERROR",
                "trigger is not supported $this"
            ).internal()
        }
    }
}
