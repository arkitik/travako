package io.arkitik.travako.adapter.job

import io.arkitik.radix.adapter.shared.StoreImpl
import io.arkitik.travako.adapter.job.creator.JobInstanceParamCreatorImpl
import io.arkitik.travako.adapter.job.query.JobInstanceParamStoreQueryImpl
import io.arkitik.travako.adapter.job.repository.TravakoJobInstanceParamRepository
import io.arkitik.travako.adapter.job.updater.JobInstanceParamUpdaterImpl
import io.arkitik.travako.core.domain.job.JobInstanceParamDomain
import io.arkitik.travako.entity.job.TravakoJobInstanceParam
import io.arkitik.travako.store.job.JobInstanceParamStore
import io.arkitik.travako.store.job.creator.JobInstanceParamCreator
import io.arkitik.travako.store.job.query.JobInstanceParamStoreQuery
import io.arkitik.travako.store.job.updater.JobInstanceParamUpdater

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:20 PM, 26/08/2024
 */
class JobInstanceParamStoreImpl(
    repository: TravakoJobInstanceParamRepository,
) : StoreImpl<String, JobInstanceParamDomain, TravakoJobInstanceParam>(
    repository
), JobInstanceParamStore {
    override val storeQuery: JobInstanceParamStoreQuery =
        JobInstanceParamStoreQueryImpl(repository)

    override fun JobInstanceParamDomain.identityUpdater(): JobInstanceParamUpdater =
        JobInstanceParamUpdaterImpl(map())

    override fun identityCreator(): JobInstanceParamCreator =
        JobInstanceParamCreatorImpl()

    override fun JobInstanceParamDomain.map() = this as TravakoJobInstanceParam
}
