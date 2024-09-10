package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.store.creator
import io.arkitik.radix.develop.store.storeUpdater
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.operation.job.roles.CheckJobRegisteredRole
import io.arkitik.travako.sdk.domain.job.JobDomainSdk
import io.arkitik.travako.sdk.domain.job.dto.JobDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.sdk.job.dto.UpdateJobParamsDto
import io.arkitik.travako.sdk.job.dto.UpdateJobTriggerDto
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.sdk.job.event.dto.JobEventKeyDto
import io.arkitik.travako.store.job.JobInstanceParamStore
import io.arkitik.travako.store.job.JobInstanceStore


/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:11 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class UpdateJobOperationProvider(
    private val jobInstanceStore: JobInstanceStore,
    private val serverDomainSdk: ServerDomainSdk,
    private val jobDomainSdk: JobDomainSdk,
    private val jobInstanceParamStore: JobInstanceParamStore,
    private val jobEventSdk: JobEventSdk,
) {
    private val checkJobRegisteredRole = CheckJobRegisteredRole(jobInstanceStore.storeQuery, serverDomainSdk)

    val updateJobTrigger =
        operationBuilder<UpdateJobTriggerDto, Unit> {
            install {
                checkJobRegisteredRole
                    .operateRole(
                        JobKeyDto(
                            serverKey = serverKey,
                            jobKey = jobKey,
                        )
                    )
            }

            mainOperation {
                val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
                with(jobInstanceStore) {
                    val jobInstance = jobDomainSdk.fetchJobInstance
                        .runOperation(JobDomainDto(server, jobKey))

                    storeUpdater(jobInstance.identityUpdater()) {
                        jobTrigger.jobTrigger()
                        nextExecution.nextExecutionTime()
                        (JobInstanceTriggerType.DURATION.takeIf { isDuration }
                            ?: JobInstanceTriggerType.CRON).jobTriggerType()
                        update()
                    }.save()
                }
                jobEventSdk.insertRestartJobEvent.runOperation(JobEventKeyDto(serverKey, jobKey))
            }
        }

    val updateJobParams =
        operationBuilder<UpdateJobParamsDto, Unit> {
            install {
                checkJobRegisteredRole
                    .operateRole(
                        JobKeyDto(
                            serverKey = serverKey,
                            jobKey = jobKey,
                        )
                    )
            }

            mainOperation {
                val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
                val jobInstance = jobDomainSdk.fetchJobInstance
                    .runOperation(JobDomainDto(server, jobKey))
                with(jobInstanceParamStore) {
                    storeQuery.findAllByJobInstance(jobInstance).deleteAll()
                    with(jobInstanceParamStore) {
                        newParams.map { (key, value) ->
                            creator(identityCreator()) {
                                key.key()
                                value.value()
                                jobInstance.job()
                            }
                        }.save()
                    }
                }
                jobEventSdk.insertRestartJobEvent.runOperation(JobEventKeyDto(serverKey, jobKey))
            }
        }
}
