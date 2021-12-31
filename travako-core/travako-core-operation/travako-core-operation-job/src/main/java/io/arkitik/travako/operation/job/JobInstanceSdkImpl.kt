package io.arkitik.travako.operation.job

import io.arkitik.travako.operation.job.operation.*
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.job.JobInstanceSdk
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
) : JobInstanceSdk {

    override val registerJob =
        RegisterJobOperationProvider(
            jobInstanceStore = jobInstanceStore,
            serverDomainSdk = serverDomainSdk
        ).registerJob

    override val removeRunnerJobsAssignee =
        RemoveRunnerJobsAssigneeOperationProvider(
            jobInstanceStore
        ).removeRunnerJobsAssignee

    override val markJobAsWaiting =
        MarkJobAsWaitingOperationProvider(
            jobInstanceStore
        ).markJobAsWaiting

    override val markJobAsRunning =
        MarkJobAsRunningOperationProvider(
            jobInstanceStore
        ).markJobAsRunning

    override val assignJobsToRunner =
        AssignJobsToRunnerOperationProvider(
            jobInstanceStore,
            schedulerRunnerDomainSdk
        ).assignJobsToRunner

    override val assignedRunnerJobs =
        AssignedRunnerJobsOperationProvider(
            jobInstanceStore.storeQuery
        ).assignedRunnerJobs

    override val isJobAssignedToRunner =
        IsJobAssignedToRunnerRole(
            jobInstanceStore.storeQuery
        )

    override val serverJobs = ServerJobsOperationProvider(
        jobInstanceStore.storeQuery
    ).serverJobs
}
