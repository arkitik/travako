package io.arkitik.travako.operation.job.event

import io.arkitik.travako.domain.job.event.embedded.JobEventType
import io.arkitik.travako.operation.job.event.operation.InsertJobEventOperationProvider
import io.arkitik.travako.operation.job.event.operation.OperationProviderMarkEventProcessedForRunner
import io.arkitik.travako.operation.job.event.operation.PendingEventsForRunnerOperationProvider
import io.arkitik.travako.operation.job.event.roles.EventIsProcessedForRunnerRole
import io.arkitik.travako.sdk.domain.job.JobDomainSdk
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.store.job.event.JobEventStore
import io.arkitik.travako.store.job.event.RunnerJobEventStateStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 6:48 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobEventSdkImpl(
    jobEventStore: JobEventStore,
    runnerJobEventStateStore: RunnerJobEventStateStore,
    jobDomainSdk: JobDomainSdk,
    schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
    serverDomainSdk: ServerDomainSdk,
) : JobEventSdk {
    override val insertRestartJobEvent =
        InsertJobEventOperationProvider(
            jobEventType = JobEventType.RESTART,
            jobDomainSdk = jobDomainSdk,
            jobEventStore = jobEventStore,
            serverDomainSdk = serverDomainSdk
        ).insertJobEvent

    override val insertRecoverJobEvent =
        InsertJobEventOperationProvider(
            jobEventType = JobEventType.RECOVER,
            jobDomainSdk = jobDomainSdk,
            jobEventStore = jobEventStore,
            serverDomainSdk = serverDomainSdk
        ).insertJobEvent

    override val insertRegisterJobEvent =
        InsertJobEventOperationProvider(
            jobEventType = JobEventType.REGISTER,
            jobDomainSdk = jobDomainSdk,
            jobEventStore = jobEventStore,
            serverDomainSdk = serverDomainSdk
        ).insertJobEvent

    override val insertDeleteJobEvent =
        InsertJobEventOperationProvider(
            jobEventType = JobEventType.DELETE,
            jobDomainSdk = jobDomainSdk,
            jobEventStore = jobEventStore,
            serverDomainSdk = serverDomainSdk
        ).insertJobEvent

    override val markEventProcessedForRunner =
        OperationProviderMarkEventProcessedForRunner(
            jobEventStore = jobEventStore,
            runnerJobEventStateStore = runnerJobEventStateStore,
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
            serverDomainSdk = serverDomainSdk
        ).markEventProcessedForRunner

    override val pendingEventsForRunner =
        PendingEventsForRunnerOperationProvider(
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
            jobEventStoreQuery = jobEventStore.storeQuery,
            runnerJobEventStateStoreQuery = runnerJobEventStateStore.storeQuery,
            serverDomainSdk = serverDomainSdk
        ).pendingEventsForRunner

    override val eventIsProcessedForRunner =
        EventIsProcessedForRunnerRole(
            jobEventStoreQuery = jobEventStore.storeQuery,
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
            runnerJobEventStateStoreQuery = runnerJobEventStateStore.storeQuery,
            serverDomainSdk = serverDomainSdk
        )
}
