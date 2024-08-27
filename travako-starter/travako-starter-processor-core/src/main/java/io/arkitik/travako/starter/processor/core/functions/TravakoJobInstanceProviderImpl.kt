package io.arkitik.travako.starter.processor.core.functions

import io.arkitik.radix.develop.shared.ext.internal
import io.arkitik.travako.starter.job.bean.TravakoJob
import io.arkitik.travako.starter.job.source.TravakoJobInstanceProvider
import io.arkitik.travako.starter.processor.core.errors.TravakoCoreStartupErrors
import io.arkitik.travako.starter.processor.core.logger.logger

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 12:59 PM, 27/08/2024
 */
internal class TravakoJobInstanceProviderImpl(
    private val providerUnits: List<TravakoJobInstanceProvider.ProviderUnit>,
) : TravakoJobInstanceProvider {
    companion object {
        private val logger = logger<TravakoJobInstanceProviderImpl>()
    }

    override fun provideJobInstance(jobKey: String, jobClassName: String): TravakoJob {
        val instanceProviderUnit = providerUnits.firstOrNull {
            it.isSupported(jobKey, jobClassName)
        }
        if (instanceProviderUnit == null) {
            logger.error(
                "No provider registered to create job instance for [key: {}, type: {}].",
                jobKey,
                jobClassName
            )
            throw TravakoCoreStartupErrors.NO_PROVIDER_TO_CREATE_JOB.internal()
        }
        return instanceProviderUnit.provideJobInstance(jobKey, jobClassName)
    }
}
