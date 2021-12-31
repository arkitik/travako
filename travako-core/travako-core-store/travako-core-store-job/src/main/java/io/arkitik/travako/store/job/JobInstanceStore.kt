package io.arkitik.travako.store.job

import io.arkitik.radix.develop.store.Store
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.store.job.creator.JobInstanceCreator
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery
import io.arkitik.travako.store.job.updater.JobInstanceUpdater

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:10 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobInstanceStore : Store<String, JobInstanceDomain> {
    override val storeQuery: JobInstanceStoreQuery

    override fun identityCreator(): JobInstanceCreator

    override fun JobInstanceDomain.identityUpdater(): JobInstanceUpdater
}
