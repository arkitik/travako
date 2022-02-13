package io.arkitik.travako.sdk.job.event

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.travako.sdk.job.event.dto.EventsDto
import io.arkitik.travako.sdk.job.event.dto.JobEventKeyDto
import io.arkitik.travako.sdk.job.event.dto.JobEventRunnerKeyAndUuidDto
import io.arkitik.travako.sdk.job.event.dto.JobEventRunnerKeyDto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 6:44 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobEventSdk {
    val insertRestartJobEvent: Operation<JobEventKeyDto, Unit>
    val insertRecoverJobEvent: Operation<JobEventKeyDto, Unit>

    val markEventProcessedForRunner: Operation<JobEventRunnerKeyAndUuidDto, Unit>

    val pendingEventsForRunner: Operation<JobEventRunnerKeyDto, EventsDto>

    val eventIsProcessedForRunner: OperationRole<JobEventRunnerKeyAndUuidDto, Boolean>
}
