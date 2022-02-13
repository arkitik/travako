package io.arkitik.travako.store.job.event

import io.arkitik.radix.develop.store.Store
import io.arkitik.travako.domain.job.event.RunnerJobEventStateDomain
import io.arkitik.travako.store.job.event.creator.RunnerJobEventStateCreator
import io.arkitik.travako.store.job.event.query.RunnerJobEventStateStoreQuery
import io.arkitik.travako.store.job.event.updater.RunnerJobEventStateUpdater

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:10 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface RunnerJobEventStateStore : Store<String, RunnerJobEventStateDomain> {
    override val storeQuery: RunnerJobEventStateStoreQuery

    override fun identityCreator(): RunnerJobEventStateCreator

    override fun RunnerJobEventStateDomain.identityUpdater(): RunnerJobEventStateUpdater
}
