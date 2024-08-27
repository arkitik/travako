package io.arkitik.travako.store.job.updater

import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import io.arkitik.travako.core.domain.job.JobInstanceParamDomain

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:18 PM, 26/08/2024
 */
interface JobInstanceParamUpdater : StoreIdentityUpdater<String, JobInstanceParamDomain> {
    fun String?.value(): JobInstanceParamUpdater
}
