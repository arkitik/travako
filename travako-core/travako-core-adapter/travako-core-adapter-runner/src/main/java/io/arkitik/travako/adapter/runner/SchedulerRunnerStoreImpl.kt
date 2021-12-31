package io.arkitik.travako.adapter.runner

import io.arkitik.radix.adapter.shared.StoreImpl
import io.arkitik.travako.adapter.runner.creator.SchedulerRunnerCreatorImpl
import io.arkitik.travako.adapter.runner.query.SchedulerRunnerStoreQueryImpl
import io.arkitik.travako.adapter.runner.repository.TravakoSchedulerRunnerRepository
import io.arkitik.travako.adapter.runner.updater.SchedulerRunnerUpdaterImpl
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.store.runner.SchedulerRunnerStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:15 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SchedulerRunnerStoreImpl(
    travakoSchedulerRunnerRepository: TravakoSchedulerRunnerRepository,
) : StoreImpl<String, SchedulerRunnerDomain, TravakoSchedulerRunner>(travakoSchedulerRunnerRepository),
    SchedulerRunnerStore {
    override fun SchedulerRunnerDomain.map() = this as TravakoSchedulerRunner

    override val storeQuery = SchedulerRunnerStoreQueryImpl(travakoSchedulerRunnerRepository)

    override fun identityCreator() =
        SchedulerRunnerCreatorImpl()

    override fun SchedulerRunnerDomain.identityUpdater() =
        SchedulerRunnerUpdaterImpl(map())
}
