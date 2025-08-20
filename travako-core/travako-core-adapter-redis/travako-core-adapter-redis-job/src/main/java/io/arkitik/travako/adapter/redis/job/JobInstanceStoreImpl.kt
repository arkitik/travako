package io.arkitik.travako.adapter.redis.job

import io.arkitik.radix.adapter.shared.StoreImpl
import io.arkitik.travako.adapter.redis.job.creator.JobInstanceCreatorImpl
import io.arkitik.travako.adapter.redis.job.query.JobInstanceStoreQueryImpl
import io.arkitik.travako.adapter.redis.job.repository.TravakoJobInstanceRepository
import io.arkitik.travako.adapter.redis.job.updater.JobInstanceUpdaterImpl
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.entity.redis.job.TravakoJobInstance
import io.arkitik.travako.store.job.JobInstanceStore
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:18 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobInstanceStoreImpl(
    travakoJobInstanceRepository: TravakoJobInstanceRepository,
) : StoreImpl<String, JobInstanceDomain, TravakoJobInstance>(travakoJobInstanceRepository), JobInstanceStore {
    override val storeQuery: JobInstanceStoreQuery =
        JobInstanceStoreQueryImpl(travakoJobInstanceRepository)

    override fun JobInstanceDomain.map() = this as TravakoJobInstance

    override fun identityCreator() =
        JobInstanceCreatorImpl()

    override fun JobInstanceDomain.identityUpdater() =
        JobInstanceUpdaterImpl(map())
}
