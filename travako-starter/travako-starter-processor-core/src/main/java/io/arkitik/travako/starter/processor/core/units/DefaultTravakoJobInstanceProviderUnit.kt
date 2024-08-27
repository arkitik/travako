package io.arkitik.travako.starter.processor.core.units

import io.arkitik.radix.develop.shared.ext.internalError
import io.arkitik.travako.starter.job.bean.TravakoJob
import io.arkitik.travako.starter.job.source.TravakoJobInstanceProvider
import io.arkitik.travako.starter.processor.core.errors.TravakoCoreStartupErrors
import io.arkitik.travako.starter.processor.core.logger.logger
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.DefaultListableBeanFactory

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:06 PM, 27/08/2024
 */
internal class DefaultTravakoJobInstanceProviderUnit(
    private val defaultListableBeanFactory: DefaultListableBeanFactory,
) : TravakoJobInstanceProvider.ProviderUnit {
    companion object {
        private val logger = logger<DefaultTravakoJobInstanceProviderUnit>()
    }

    override fun isSupported(jobKey: String, jobClassName: String): Boolean {
        val jobClass = runCatching {
            Class.forName(jobClassName)
        }.getOrNull()

        return jobClass != null && TravakoJob::class.java.isAssignableFrom(jobClass)
    }

    override fun provideJobInstance(jobKey: String, jobClassName: String): TravakoJob {
        val jobClass = runCatching {
            Class.forName(jobClassName)
        }.getOrNull().internalError(TravakoCoreStartupErrors.JOB_CLASS_NOT_FOUND)
        return fetchOrRegisterJobBean(jobKey, jobClass)
    }


    private fun fetchOrRegisterJobBean(jobKey: String, clazz: Class<*>): TravakoJob {
        return runCatching {
            defaultListableBeanFactory.getBean(clazz) as TravakoJob
        }.getOrElse {
            logger.warn(
                "Trying to create TravakoJob for [key: {} of type: {}]",
                jobKey,
                clazz.name,
            )
            registerAndGetJobBean(jobKey, clazz)
        }
    }

    private fun registerAndGetJobBean(jobKey: String, clazz: Class<*>): TravakoJob {
        return runCatching {
            val genericBeanDefinition =
                BeanDefinitionBuilder.genericBeanDefinition(clazz).beanDefinition
            defaultListableBeanFactory.registerBeanDefinition(jobKey, genericBeanDefinition)
            defaultListableBeanFactory.getBean(jobKey, clazz) as TravakoJob
        }.onFailure {
            logger.error(
                "Error while creating TravakoJob for [key: {} of type: {}]",
                jobKey,
                clazz.name,
                it
            )
        }.getOrThrow()
    }
}
