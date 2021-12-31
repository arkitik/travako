package io.arkitik.travako.operation.runner

import io.arkitik.travako.operation.runner.operation.*
import io.arkitik.travako.sdk.domain.leader.LeaderDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.store.runner.SchedulerRunnerStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:25 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SchedulerRunnerSdkImpl(
    schedulerRunnerStore: SchedulerRunnerStore,
    jobInstanceSdk: JobInstanceSdk,
    serverDomainSdk: ServerDomainSdk,
    leaderDomainSdk: LeaderDomainSdk
) : SchedulerRunnerSdk {

    override val registerRunner = RegisterRunnerOperationProvider(
        schedulerRunnerStore = schedulerRunnerStore,
        serverDomainSdk = serverDomainSdk,
    ).registerRunnerOperation

    override val logRunnerHeartbeat = LogRunnerHeartbeatOperationProvider(
        schedulerRunnerStore = schedulerRunnerStore,
    ).logRunnerHeartbeatOperation

    override val unregisterRunner = UnRegisterRunnerOperationProvider(
        schedulerRunnerStore = schedulerRunnerStore,
        jobInstanceSdk = jobInstanceSdk,
    ).unRegisterRunnerOperation

    override val markRunnerAsDown =
        ChangeRunnerStateToDownOperationProvider(
            schedulerRunnerStore = schedulerRunnerStore,
        ).changeRunnerToDownState

    override val markRunnerAsUp =
        ChangeRunnerStateToUpOperationProvider(
            schedulerRunnerStore = schedulerRunnerStore,
        ).changeRunnerStateToUp

    override val allServerRunners =
        ServerRunnersOperationProvider(
            schedulerRunnerStore.storeQuery,
            leaderDomainSdk
        ).serverRunners
}
