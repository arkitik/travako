package io.arkitik.travako.store.job.query

import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.JobInstanceParamDomain

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:20 PM, 26/08/2024
 */
interface JobInstanceParamStoreQuery : StoreQuery<String, JobInstanceParamDomain> {
    fun findAllByJobInstance(jobInstance: JobInstanceDomain): List<JobInstanceParamDomain>
}
