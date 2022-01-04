package io.arkitik.travako.starter.processor.scheduler

import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.support.CronTrigger
import java.time.Duration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29 2:25 AM, **Wed, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */

fun Duration.fixedRateJob(
    taskScheduler: TaskScheduler,
    taskCommand: () -> Unit,
) = taskScheduler.scheduleAtFixedRate(taskCommand, this)

fun String.cronJob(
    taskScheduler: TaskScheduler,
    taskCommand: () -> Unit,
) = taskScheduler.schedule(taskCommand, CronTrigger(this))

fun Trigger.buildJob(
    taskScheduler: TaskScheduler,
    taskCommand: () -> Unit,
) = taskScheduler.schedule(taskCommand, this)
