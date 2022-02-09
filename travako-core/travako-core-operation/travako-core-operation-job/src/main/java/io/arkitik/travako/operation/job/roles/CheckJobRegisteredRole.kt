package io.arkitik.travako.operation.job.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.operation.job.errors.JobErrors
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:03 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class CheckJobRegisteredRole(
    private val jobInstanceStoreQuery: JobInstanceStoreQuery,
    private val serverDomainSdk: ServerDomainSdk,
) : OperationRole<JobKeyDto, Unit> {
    override fun JobKeyDto.operateRole() {
        val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
        jobInstanceStoreQuery.existsByServerAndJobKey(
            server = server,
            jobKey = jobKey
        ).takeIf { !it }?.also {
            throw JobErrors.JOB_IS_NOT_REGISTERED.unprocessableEntity()
        }
    }
}
