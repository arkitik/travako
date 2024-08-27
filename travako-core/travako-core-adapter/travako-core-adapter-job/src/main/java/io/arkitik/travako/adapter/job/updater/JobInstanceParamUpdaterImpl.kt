package io.arkitik.travako.adapter.job.updater

import io.arkitik.travako.entity.job.TravakoJobInstanceParam
import io.arkitik.travako.store.job.updater.JobInstanceParamUpdater

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:25 PM, 26/08/2024
 */
internal class JobInstanceParamUpdaterImpl(
    private val jobInstanceParam: TravakoJobInstanceParam,
) : JobInstanceParamUpdater {
    override fun String?.value(): JobInstanceParamUpdater {
        jobInstanceParam.value = this
        return this@JobInstanceParamUpdaterImpl
    }

    override fun update() = jobInstanceParam
}
