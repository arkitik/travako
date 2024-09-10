package io.arkitik.travako.starter.processor.core.functions

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.CreateJobDto
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.sdk.job.dto.UpdateJobParamsDto
import io.arkitik.travako.sdk.job.dto.UpdateJobTriggerDto
import io.arkitik.travako.starter.job.registry.JobInstancesRegistry
import io.arkitik.travako.starter.job.registry.dto.TravakoJobBeanData
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import io.arkitik.travako.starter.processor.core.job.nextTimeToExecution
import io.arkitik.travako.starter.processor.core.job.parseTrigger
import io.arkitik.travako.starter.processor.core.logger.logger
import org.springframework.scheduling.Trigger
import java.time.LocalDate
import java.time.ZoneId


/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 9:40 PM, 25/08/2024
 */
internal class JobInstancesRegistryImpl(
    private val jobInstanceSdk: JobInstanceSdk,
    private val travakoConfig: TravakoConfig,
) : JobInstancesRegistry {
    companion object {
        private val logger = logger<JobInstancesRegistryImpl>()
    }

    override fun registerJob(jobBeanData: TravakoJobBeanData) {
        val jobTrigger = jobBeanData.jobTrigger.parseTrigger()
        val nextExecution = jobBeanData.jobTrigger.nextTimeToExecution(
            jobBeanData.firingTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()
        )
        jobInstanceSdk.registerJob
            .runOperation(
                CreateJobDto(
                    serverKey = travakoConfig.serverKey,
                    jobKey = jobBeanData.jobKey,
                    jobTrigger = jobTrigger.first,
                    isDuration = jobTrigger.second,
                    nextExecution = nextExecution,
                    jobClassName = jobBeanData.jobClass.name,
                    params = jobBeanData.params,
                    singleRun = jobBeanData.singleRun
                )
            )
        logger.info(
            "Job with key: {} has been registered, the registration event has been published and runners will start the execution shortly",
            jobBeanData.jobKey
        )
    }

    override fun unregisterJob(jobKey: String) {
        jobInstanceSdk.unregisterJob
            .runOperation(
                JobKeyDto(
                    serverKey = travakoConfig.serverKey,
                    jobKey = jobKey,
                )
            )

        logger.info(
            "Job with key: {} has been unregistered, Deletion event has been published and runners will delete the job shortly",
            jobKey
        )
    }

    override fun updateJobParams(jobKey: String, params: Map<String, String?>) {
        jobInstanceSdk.updateJobParams
            .runOperation(
                UpdateJobParamsDto(
                    serverKey = travakoConfig.serverKey,
                    jobKey = jobKey,
                    newParams = params
                )
            )

        logger.info(
            "Job with key: {} params has been updated, Restart event has been published and runners will update the execution trigger shortly",
            jobKey
        )
    }

    override fun updateJobTrigger(jobKey: String, jobTrigger: Trigger) {
        val triggerPair = jobTrigger.parseTrigger()
        val nextExecution = jobTrigger.nextTimeToExecution()
        jobInstanceSdk.updateJobTrigger
            .runOperation(
                UpdateJobTriggerDto(
                    serverKey = travakoConfig.serverKey,
                    jobKey = jobKey,
                    jobTrigger = triggerPair.first,
                    isDuration = triggerPair.second,
                    nextExecution = nextExecution,
                )
            )


        logger.info(
            "Job with key: {} trigger has been updated, Restart event has been published and runners will update the execution trigger shortly",
            jobKey
        )
    }

    override fun jobRegistered(jobKey: String) =
        jobInstanceSdk.jobRegistered.operateRole(
            JobKeyDto(
                serverKey = travakoConfig.serverKey,
                jobKey = jobKey
            )
        )
}
