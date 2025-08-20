package io.arkitik.travako.entity.exposed.job

import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.JobInstanceParamDomain
import org.jetbrains.exposed.sql.Database
import java.time.LocalDateTime

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:10 PM, 26/08/2024
 */
data class TravakoJobInstanceParam(
    override val uuid: String,
    val jobUuid: String,
    private val jobInstanceTable: TravakoJobInstanceTable,
    private val database: Database? = null,
    override val key: String,
    override var value: String?,
    override val creationDate: LocalDateTime = LocalDateTime.now(),
) : JobInstanceParamDomain {
    override val job: JobInstanceDomain by lazy {
        jobInstanceTable.findIdentityByUuid(jobUuid, database)!!
    }
}
