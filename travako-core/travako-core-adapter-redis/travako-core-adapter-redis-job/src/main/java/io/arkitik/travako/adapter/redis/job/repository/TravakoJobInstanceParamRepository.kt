package io.arkitik.travako.adapter.redis.job.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.core.domain.job.JobInstanceParamDomain
import io.arkitik.travako.entity.redis.job.TravakoJobInstanceParam

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:22 PM, 26/08/2024
 */
interface TravakoJobInstanceParamRepository : RadixRepository<String, TravakoJobInstanceParam> {
    fun findAllByJobUuid(jobUuid: String): List<JobInstanceParamDomain>
}
