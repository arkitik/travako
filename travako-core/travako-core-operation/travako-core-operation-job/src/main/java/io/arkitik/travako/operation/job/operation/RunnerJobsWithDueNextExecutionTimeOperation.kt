package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.job.dto.JobDetails
import io.arkitik.travako.sdk.job.dto.JobServerRunnerKeyNextExecutionDto
import io.arkitik.travako.store.job.query.JobInstanceParamStoreQuery
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 10:47 PM, 25/08/2024
 */
internal class RunnerJobsWithDueNextExecutionTimeOperation(
    private val jobInstanceStoreQuery: JobInstanceStoreQuery,
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
    private val serverDomainSdk: ServerDomainSdk,
    private val jobInstanceParamStoreQuery: JobInstanceParamStoreQuery,
) : Operation<JobServerRunnerKeyNextExecutionDto, List<JobDetails>> {
    override fun JobServerRunnerKeyNextExecutionDto.operate(): List<JobDetails> {
        val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
        val schedulerRunner = schedulerRunnerDomainSdk.fetchSchedulerRunner
            .runOperation(
                RunnerDomainDto(
                    server = server,
                    runnerKey = runnerKey,
                    runnerHost = runnerHost
                )
            )
        return jobInstanceStoreQuery.findAllByServerAndRunnerAndStatusNextExecutionTimeIsBefore(
            nextExecutionTime = executionTime,
            status = JobStatus.WAITING,
            server = server,
            runner = schedulerRunner,
        ).map { job ->
            val jobInstanceParams = jobInstanceParamStoreQuery.findAllByJobInstance(job)
                .associate { it.key to it.value }
            JobDetails(
                jobKey = job.jobKey,
                jobClassName = job.jobClassName,
                isRunning = JobStatus.RUNNING == job.jobStatus,
                jobTrigger = job.jobTrigger,
                isDuration = JobInstanceTriggerType.DURATION == job.jobTriggerType,
                lastRunningTime = job.lastRunningTime,
                params = jobInstanceParams,
                singleRun = job.singleRun,
            )
        }
    }
}
