package io.arkitik.travako.adapter.job.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.job.repository.TravakoJobInstanceParamRepository
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.JobInstanceParamDomain
import io.arkitik.travako.entity.job.TravakoJobInstanceParam
import io.arkitik.travako.store.job.query.JobInstanceParamStoreQuery

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:21 PM, 26/08/2024
 */
internal class JobInstanceParamStoreQueryImpl(
    private val travakoJobInstanceParamRadixRepository: TravakoJobInstanceParamRepository,
) : StoreQueryImpl<String, JobInstanceParamDomain, TravakoJobInstanceParam>(travakoJobInstanceParamRadixRepository),
    JobInstanceParamStoreQuery {
    override fun findAllByJobInstance(jobInstance: JobInstanceDomain) =
        travakoJobInstanceParamRadixRepository.findAllByJob(jobInstance)
}
