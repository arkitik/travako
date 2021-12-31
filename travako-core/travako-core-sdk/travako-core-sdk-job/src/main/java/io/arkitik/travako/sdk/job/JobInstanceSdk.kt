package io.arkitik.travako.sdk.job

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.travako.sdk.job.dto.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 6:44 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobInstanceSdk {
    val registerJob: Operation<JobKeyDto, Unit>
    val removeRunnerJobsAssignee: Operation<JobRunnerKeyDto, Unit>

    val markJobAsWaiting: Operation<JobKeyDto, Unit>
    val markJobAsRunning: Operation<JobKeyDto, Unit>

    val assignJobsToRunner: Operation<AssignJobsToRunnerDto, Unit>

    val assignedRunnerJobs: Operation<JobRunnerKeyDto, AssignJobsToRunnerDto>

    val isJobAssignedToRunner: OperationRole<JobServerRunnerKeyDto, Boolean>

    val serverJobs: Operation<JobServerDto, List<JobDetails>>
}
