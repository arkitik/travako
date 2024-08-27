package io.arkitik.travako.operation.runner

import io.arkitik.travako.operation.runner.operation.ChangeRunnerStateToDownOperationProvider
import io.arkitik.travako.operation.runner.operation.ChangeRunnerStateToUpOperationProvider
import io.arkitik.travako.operation.runner.operation.LogRunnerHeartbeatOperationProvider
import io.arkitik.travako.operation.runner.operation.RegisterRunnerOperationProvider
import io.arkitik.travako.operation.runner.operation.RunnerDetailsOperationProvider
import io.arkitik.travako.operation.runner.operation.ServerRunnersOperationProvider
import io.arkitik.travako.sdk.domain.leader.LeaderDomainSdk
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.store.runner.SchedulerRunnerStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:25 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SchedulerRunnerSdkImpl(
    schedulerRunnerStore: SchedulerRunnerStore,
    serverDomainSdk: ServerDomainSdk,
    leaderDomainSdk: LeaderDomainSdk,
    schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
) : SchedulerRunnerSdk {

    override val registerRunner = RegisterRunnerOperationProvider(
        schedulerRunnerStore = schedulerRunnerStore,
        serverDomainSdk = serverDomainSdk,
        schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
    ).registerRunnerOperation

    override val logRunnerHeartbeat = LogRunnerHeartbeatOperationProvider(
        schedulerRunnerStore = schedulerRunnerStore,
        serverDomainSdk = serverDomainSdk,
        schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
    ).logRunnerHeartbeatOperation

    override val markRunnerAsDown =
        ChangeRunnerStateToDownOperationProvider(
            schedulerRunnerStore = schedulerRunnerStore,
            serverDomainSdk = serverDomainSdk,
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
        ).changeRunnerToDownState

    override val markRunnerAsUp =
        ChangeRunnerStateToUpOperationProvider(
            schedulerRunnerStore = schedulerRunnerStore,
            serverDomainSdk = serverDomainSdk,
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
        ).changeRunnerStateToUp

    override val allServerRunners =
        ServerRunnersOperationProvider(
            schedulerRunnerStoreQuery = schedulerRunnerStore.storeQuery,
            leaderDomainSdk = leaderDomainSdk,
            serverDomainSdk = serverDomainSdk,
        ).serverRunners

    override val allRunningServerRunners =
        ServerRunnersOperationProvider(
            schedulerRunnerStoreQuery = schedulerRunnerStore.storeQuery,
            leaderDomainSdk = leaderDomainSdk,
            serverDomainSdk = serverDomainSdk,
        ).allRunningServerRunners

    override val runnerDetails = RunnerDetailsOperationProvider(
        leaderDomainSdk = leaderDomainSdk,
        serverDomainSdk = serverDomainSdk,
        schedulerRunnerDomainSdk = schedulerRunnerDomainSdk
    ).runnerDetails
}
