package io.arkitik.travako.store.job.event.updater

import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import io.arkitik.travako.domain.job.event.JobEventDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:11 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobEventUpdater : StoreIdentityUpdater<String, JobEventDomain> {
    fun processed(): JobEventUpdater
}
