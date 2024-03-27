package io.arkitik.travako.operation.job

import io.arkitik.travako.operation.job.operation.AssignJobsToRunnerOperationProvider
import io.arkitik.travako.operation.job.operation.AssignedRunnerJobsOperationProvider
import io.arkitik.travako.operation.job.operation.IsJobAssignedToRunnerRole
import io.arkitik.travako.operation.job.operation.MarkJobAsRunningOperationProvider
import io.arkitik.travako.operation.job.operation.MarkJobAsWaitingOperationProvider
import io.arkitik.travako.operation.job.operation.RegisterJobOperationProvider
import io.arkitik.travako.operation.job.operation.RemoveRunnerJobsAssigneeOperationProvider
import io.arkitik.travako.operation.job.operation.ServerJobsOperationProvider
import io.arkitik.travako.operation.job.operation.UpdateJobTriggerOperationProvider
import io.arkitik.travako.sdk.domain.job.JobDomainSdk
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.event.JobEventSdk
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
) : JobInstanceSdk {

    override val registerJob =
        RegisterJobOperationProvider(
            jobInstanceStore = jobInstanceStore,
            serverDomainSdk = serverDomainSdk
        ).registerJob
    override val updateJobTrigger =
        UpdateJobTriggerOperationProvider(
            jobInstanceStore = jobInstanceStore,
            jobEventSdk = jobEventSdk,
            serverDomainSdk = serverDomainSdk,
            jobDomainSdk = jobDomainSdk,
        ).updateJobTrigger

    override val removeRunnerJobsAssignee =
        RemoveRunnerJobsAssigneeOperationProvider(
            jobInstanceStore = jobInstanceStore,
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
            serverDomainSdk = serverDomainSdk
        ).removeRunnerJobsAssignee

    override val markJobAsWaiting =
        MarkJobAsWaitingOperationProvider(
            jobInstanceStore = jobInstanceStore,
            serverDomainSdk = serverDomainSdk,
            jobDomainSdk = jobDomainSdk,
        ).markJobAsWaiting

    override val markJobAsRunning =
        MarkJobAsRunningOperationProvider(
            jobInstanceStore = jobInstanceStore,
            serverDomainSdk = serverDomainSdk,
            jobDomainSdk = jobDomainSdk,
        ).markJobAsRunning

    override val assignJobsToRunner =
        AssignJobsToRunnerOperationProvider(
            jobInstanceStore = jobInstanceStore,
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
            serverDomainSdk = serverDomainSdk
        ).assignJobsToRunner

    override val assignedRunnerJobs =
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
        serverDomainSdk = serverDomainSdk
    ).serverJobs
}
