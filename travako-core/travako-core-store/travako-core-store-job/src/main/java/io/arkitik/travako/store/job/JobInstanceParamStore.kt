package io.arkitik.travako.store.job

import io.arkitik.radix.develop.store.Store
import io.arkitik.travako.core.domain.job.JobInstanceParamDomain
import io.arkitik.travako.store.job.creator.JobInstanceParamCreator
import io.arkitik.travako.store.job.query.JobInstanceParamStoreQuery
import io.arkitik.travako.store.job.updater.JobInstanceParamUpdater

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:17 PM, 26/08/2024
 */
interface JobInstanceParamStore : Store<String, JobInstanceParamDomain> {
    override fun JobInstanceParamDomain.identityUpdater(): JobInstanceParamUpdater

    override fun identityCreator(): JobInstanceParamCreator

    override val storeQuery: JobInstanceParamStoreQuery
}
