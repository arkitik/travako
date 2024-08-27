package io.arkitik.travako.operation.job.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 11:23 AM, 26/08/2024
 */
internal class JobRegisteredRole(
    private val jobInstanceStoreQuery: JobInstanceStoreQuery,
    private val serverDomainSdk: ServerDomainSdk,
) : OperationRole<JobKeyDto, Boolean> {
    override fun JobKeyDto.operateRole(): Boolean {
        val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
        return jobInstanceStoreQuery.existsByServerAndJobKeyAndStatusIn(
            server = server,
            jobKey = jobKey,
            statuses = JobStatus.entries.filterNot { it == JobStatus.DOWN }
        )
    }
}
