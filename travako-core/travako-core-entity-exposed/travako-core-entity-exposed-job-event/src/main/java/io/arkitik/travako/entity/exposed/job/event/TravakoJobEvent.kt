package io.arkitik.travako.entity.exposed.job.event

import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.embedded.JobEventType
import io.arkitik.travako.entity.exposed.job.TravakoJobInstanceTable
import org.jetbrains.exposed.sql.Database
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 03 11:29 PM, **Mon, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class TravakoJobEvent(
    override val uuid: String,
    override val eventType: JobEventType,
    val jobInstanceUuid: String,
    override var processedFlag: Boolean = false,
    override val creationDate: LocalDateTime = LocalDateTime.now(),
    private val jobInstanceTable: TravakoJobInstanceTable,
    private val database: Database?,
) : JobEventDomain {
    override val jobInstance: JobInstanceDomain by lazy {
        jobInstanceTable.findIdentityByUuid(jobInstanceUuid, database)!!
    }
}
