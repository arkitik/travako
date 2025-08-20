package io.arkitik.travako.adapter.redis.job.updater

import io.arkitik.travako.entity.redis.job.TravakoJobInstanceParam
import io.arkitik.travako.store.job.updater.JobInstanceParamUpdater

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:25 PM, 26/08/2024
 */
internal class JobInstanceParamUpdaterImpl(
    private val jobInstanceParam: TravakoJobInstanceParam,
) : JobInstanceParamUpdater {

    override fun update() = jobInstanceParam
}
