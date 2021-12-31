package io.arkitik.travako.adapter.job

import io.arkitik.radix.adapter.shared.StoreImpl
import io.arkitik.travako.adapter.job.creator.JobInstanceCreatorImpl
import io.arkitik.travako.adapter.job.query.JobInstanceStoreQueryImpl
import io.arkitik.travako.adapter.job.repository.TravakoJobInstanceRepository
import io.arkitik.travako.adapter.job.updater.JobInstanceUpdaterImpl
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.entity.job.TravakoJobInstance
import io.arkitik.travako.store.job.JobInstanceStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:18 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobInstanceStoreImpl(
    travakoJobInstanceRepository: TravakoJobInstanceRepository,
) : StoreImpl<String, JobInstanceDomain, TravakoJobInstance>(travakoJobInstanceRepository), JobInstanceStore {
    override val storeQuery =
        JobInstanceStoreQueryImpl(travakoJobInstanceRepository)

    override fun JobInstanceDomain.map() = this as TravakoJobInstance

    override fun identityCreator() =
        JobInstanceCreatorImpl()

    override fun JobInstanceDomain.identityUpdater() =
        JobInstanceUpdaterImpl(map())
}
