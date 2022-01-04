package io.arkitik.travako.adapter.job.event

import io.arkitik.radix.adapter.shared.StoreImpl
import io.arkitik.travako.adapter.job.event.creator.RunnerJobEventStateCreatorImpl
import io.arkitik.travako.adapter.job.event.query.RunnerJobEventStateStoreQueryImpl
import io.arkitik.travako.adapter.job.event.repository.TravakoRunnerJobEventStateRepository
import io.arkitik.travako.adapter.job.event.updater.RunnerJobEventStateUpdaterImpl
import io.arkitik.travako.domain.job.event.RunnerJobEventStateDomain
import io.arkitik.travako.entity.job.event.TravakoRunnerJobEventState
import io.arkitik.travako.store.job.event.RunnerJobEventStateStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 5:05 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RunnerJobEventStateStoreImpl(
    travakoRunnerJobEventStateRepository: TravakoRunnerJobEventStateRepository,
) : StoreImpl<String, RunnerJobEventStateDomain, TravakoRunnerJobEventState>(
    travakoRunnerJobEventStateRepository
), RunnerJobEventStateStore {
    override fun RunnerJobEventStateDomain.map() = this as TravakoRunnerJobEventState

    override val storeQuery =
        RunnerJobEventStateStoreQueryImpl(travakoRunnerJobEventStateRepository)

    override fun identityCreator() = RunnerJobEventStateCreatorImpl()

    override fun RunnerJobEventStateDomain.identityUpdater() =
        RunnerJobEventStateUpdaterImpl(map())

}
