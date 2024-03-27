package io.arkitik.travako.sdk.runner

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.travako.sdk.runner.dto.RunnerDetails
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.sdk.runner.dto.RunnerServerKeyDto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 7:54 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface SchedulerRunnerSdk {
    val registerRunner: Operation<RunnerKeyDto, Unit>

    val logRunnerHeartbeat: Operation<RunnerKeyDto, Unit>

    val markRunnerAsDown: Operation<RunnerKeyDto, Unit>

    val markRunnerAsUp: Operation<RunnerKeyDto, Unit>

    val allServerRunners: Operation<RunnerServerKeyDto, List<RunnerDetails>>

    val runnerDetails: Operation<RunnerKeyDto, RunnerDetails>
}
