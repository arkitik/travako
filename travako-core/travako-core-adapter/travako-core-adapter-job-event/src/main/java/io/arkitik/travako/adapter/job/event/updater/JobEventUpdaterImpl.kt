package io.arkitik.travako.adapter.job.event.updater

import io.arkitik.travako.entity.job.event.TravakoJobEvent
import io.arkitik.travako.store.job.event.updater.JobEventUpdater

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 4:54 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobEventUpdaterImpl(
    private val jobEvent: TravakoJobEvent,
) : JobEventUpdater {
    override fun processed(): JobEventUpdater {
        jobEvent.processedFlag = true
        return this@JobEventUpdaterImpl
    }

    override fun update() = jobEvent
}
