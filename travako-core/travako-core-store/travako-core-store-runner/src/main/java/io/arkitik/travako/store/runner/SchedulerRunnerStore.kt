package io.arkitik.travako.store.runner

import io.arkitik.radix.develop.store.Store
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.store.runner.creator.SchedulerRunnerCreator
import io.arkitik.travako.store.runner.query.SchedulerRunnerStoreQuery
import io.arkitik.travako.store.runner.updater.SchedulerRunnerUpdater

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:10 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface SchedulerRunnerStore : Store<String, SchedulerRunnerDomain> {
    override val storeQuery: SchedulerRunnerStoreQuery
    override fun identityCreator(): SchedulerRunnerCreator
    override fun SchedulerRunnerDomain.identityUpdater(): SchedulerRunnerUpdater
}
