package io.arkitik.travako.adapter.exposed.job.event.updater

import io.arkitik.travako.entity.exposed.job.event.TravakoRunnerJobEventState
import io.arkitik.travako.store.job.event.updater.RunnerJobEventStateUpdater

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 5:09 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class RunnerJobEventStateUpdaterImpl(
    private val travakoRunnerJobEventState: TravakoRunnerJobEventState,
) : RunnerJobEventStateUpdater {
    override fun update() = travakoRunnerJobEventState
}
