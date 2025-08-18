package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.store.creator
import io.arkitik.radix.develop.store.creatorWithInsert
import io.arkitik.radix.develop.store.storeUpdaterWithUpdate
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.operation.job.roles.CheckRegisteredJobRole
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.job.dto.CreateJobDto
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.sdk.job.event.dto.JobEventKeyDto
import io.arkitik.travako.store.job.JobInstanceParamStore
import io.arkitik.travako.store.job.JobInstanceStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:11 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RegisterJobOperationProvider(
    private val jobInstanceStore: JobInstanceStore,
    private val jobInstanceParamStore: JobInstanceParamStore,
    private val serverDomainSdk: ServerDomainSdk,
    private val jobEventSdk: JobEventSdk,
) {
    private val checkRegisteredJobRole = CheckRegisteredJobRole(
        jobInstanceStoreQuery = jobInstanceStore.storeQuery,
        serverDomainSdk = serverDomainSdk
    )
    val registerJob = operationBuilder<CreateJobDto, Unit> {
        install {
            checkRegisteredJobRole.operateRole(JobKeyDto(serverKey = serverKey, jobKey = jobKey))
        }
        mainOperation {
            val server = serverDomainSdk.fetchServer.runOperation(
                ServerDomainDto(serverKey)
            )
            with(jobInstanceStore) {
                val oldJobInstanceDomain = storeQuery.findByServerAndJobKey(
                    server = server,
                    jobKey = jobKey
                )
                if (oldJobInstanceDomain != null) {
                    storeUpdaterWithUpdate(oldJobInstanceDomain.identityUpdater()) {
                        jobClassName.jobClassName()
                        JobStatus.WAITING.jobStatus()
                        nextExecution.nextExecutionTime()
                        jobTrigger.jobTrigger()
                        singleRun.singleRun()
                        (JobInstanceTriggerType.DURATION.takeIf { isDuration }
                            ?: JobInstanceTriggerType.CRON).jobTriggerType()
                        update()
                    }.also { jobInstanceDomain ->
                        with(jobInstanceParamStore) {
                            storeQuery.findAllByJobInstance(oldJobInstanceDomain).deleteAll()
                            params.map { (key, value) ->
                                creator(identityCreator()) {
                                    key.key()
                                    value.value()
                                    jobInstanceDomain.job()
                                }
                            }.insertIgnore()
                        }
                    }

                } else {
                    creatorWithInsert(identityCreator()) {
                        jobKey.jobKey()
                        singleRun.singleRun()
                        jobClassName.jobClassName()
                        JobStatus.WAITING.jobStatus()
                        server.server()
                        nextExecution.nextExecutionTime()
                        jobTrigger.jobTrigger()
                        (JobInstanceTriggerType.DURATION.takeIf { isDuration }
                            ?: JobInstanceTriggerType.CRON).jobTriggerType()
                    }.also { jobInstanceDomain ->
                        with(jobInstanceParamStore) {
                            params.map { (key, value) ->
                                creator(identityCreator()) {
                                    key.key()
                                    value.value()
                                    jobInstanceDomain.job()
                                }
                            }.insertIgnore()
                        }
                    }
                }
            }
            jobEventSdk.insertRegisterJobEvent.runOperation(JobEventKeyDto(serverKey, jobKey))
        }
    }
}
