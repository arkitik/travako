package io.arkitik.travako.store.job.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.JobInstanceParamDomain

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:18 PM, 26/08/2024
 */
interface JobInstanceParamCreator : StoreIdentityCreator<String, JobInstanceParamDomain> {
    fun JobInstanceDomain.job(): JobInstanceParamCreator
    fun String.key(): JobInstanceParamCreator
    fun String?.value(): JobInstanceParamCreator
}
