package io.arkitik.travako.operation.job

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.travako.operation.job.operation.*
import io.arkitik.travako.operation.job.roles.JobRegisteredRole
import io.arkitik.travako.sdk.domain.job.JobDomainSdk
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.*
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.store.job.JobInstanceParamStore
import io.arkitik.travako.store.job.JobInstanceStore


/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 6:48 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobInstanceSdkImpl(
    jobInstanceStore: JobInstanceStore,
    serverDomainSdk: ServerDomainSdk,
    schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
    jobEventSdk: JobEventSdk,
    jobDomainSdk: JobDomainSdk,
    jobInstanceParamStore: JobInstanceParamStore,
) : JobInstanceSdk {
    private val updateJobOperationProvider = UpdateJobOperationProvider(
        jobInstanceStore = jobInstanceStore,
        serverDomainSdk = serverDomainSdk,
        jobDomainSdk = jobDomainSdk,
        jobInstanceParamStore = jobInstanceParamStore,
        jobEventSdk = jobEventSdk
    )

    override val registerJob =
        RegisterJobOperationProvider(
            jobInstanceStore = jobInstanceStore,
            serverDomainSdk = serverDomainSdk,
            jobInstanceParamStore = jobInstanceParamStore,
            jobEventSdk = jobEventSdk
        ).registerJob

    override val updateJobTrigger: Operation<UpdateJobTriggerDto, Unit> =
        updateJobOperationProvider.updateJobTrigger

    override val updateJobParams: Operation<UpdateJobParamsDto, Unit> =
        updateJobOperationProvider.updateJobParams

    override val removeRunnerJobsAssignee =
        RemoveRunnerJobsAssigneeOperationProvider(
            jobInstanceStore = jobInstanceStore,
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
            serverDomainSdk = serverDomainSdk
        ).removeRunnerJobsAssignee

    override val markJobAsWaiting: Operation<UpdateJobRequest, Unit> =
        MarkJobAsWaitingOperationProvider(
            jobInstanceStore = jobInstanceStore,
            serverDomainSdk = serverDomainSdk,
            jobDomainSdk = jobDomainSdk,
        ).markJobAsWaiting

    override val markJobAsRunning: Operation<UpdateJobRequest, Unit> =
        MarkJobAsRunningOperationProvider(
            jobInstanceStore = jobInstanceStore,
            serverDomainSdk = serverDomainSdk,
            jobDomainSdk = jobDomainSdk,
        ).markJobAsRunning

    override val markJobAsDone: Operation<JobKeyDto, Unit> =
        MarkJobAsDoneWaitingOperationProvider(
            jobInstanceStore = jobInstanceStore,
            serverDomainSdk = serverDomainSdk,
            jobDomainSdk = jobDomainSdk,
            jobEventSdk = jobEventSdk,
        ).markJobAsDone

    override val assignJobsToRunner =
        AssignJobsToRunnerOperationProvider(
            jobInstanceStore = jobInstanceStore,
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
            serverDomainSdk = serverDomainSdk
        ).assignJobsToRunner

    override val assignedRunnerJobs: Operation<JobRunnerKeyDto, AssignedJobsToRunnerDto> =
        AssignedRunnerJobsOperationProvider(
            jobInstanceStoreQuery = jobInstanceStore.storeQuery,
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
            serverDomainSdk = serverDomainSdk
        ).assignedRunnerJobs

    override val isJobAssignedToRunner =
        IsJobAssignedToRunnerRole(
            jobInstanceStoreQuery = jobInstanceStore.storeQuery,
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
            serverDomainSdk = serverDomainSdk
        )

    override val serverJobs = ServerJobsOperationProvider(
        jobInstanceStoreQuery = jobInstanceStore.storeQuery,
        serverDomainSdk = serverDomainSdk,
        jobInstanceParamStoreQuery = jobInstanceParamStore.storeQuery,
    ).serverJobs
    override val jobRegistered: OperationRole<JobKeyDto, Boolean> =
        JobRegisteredRole(
            jobInstanceStoreQuery = jobInstanceStore.storeQuery,
            serverDomainSdk = serverDomainSdk
        )

    override val runnerJobsWithDueNextExecutionTime: Operation<JobServerRunnerKeyNextExecutionDto, List<JobDetails>> =
        operationBuilder {
            mainOperation(
                RunnerJobsWithDueNextExecutionTimeOperation(
                    jobInstanceStoreQuery = jobInstanceStore.storeQuery,
                    schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
                    serverDomainSdk = serverDomainSdk,
                    jobInstanceParamStoreQuery = jobInstanceParamStore.storeQuery,
                )
            )
        }
    override val unregisterJob: Operation<JobKeyDto, Unit> =
        operationBuilder {
            mainOperation(
                UnregisterJobOperation(
                    jobInstanceStore = jobInstanceStore,
                    serverDomainSdk = serverDomainSdk,
                    jobEventSdk = jobEventSdk
                )
            )
        }
    override val jobDetails: Operation<JobKeyDto, JobDetails> =
        operationBuilder {
            mainOperation(
                JobDetailsOperation(
                    serverDomainSdk = serverDomainSdk,
                    jobDomainSdk = jobDomainSdk,
                    jobInstanceParamStoreQuery = jobInstanceParamStore.storeQuery,
                )
            )
        }
}
