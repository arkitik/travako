package io.arkitik.travako.entity.redis.job

import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.JobInstanceParamDomain
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Reference
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDateTime

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:10 PM, 26/08/2024
 */
@RedisHash("travako:job:instance:param")
data class TravakoJobInstanceParam(
    @Id
    override val uuid: String,
    @Reference
    val jobInstance: TravakoJobInstance?,
    override val key: String,
    override var value: String?,
    override val creationDate: LocalDateTime = LocalDateTime.now(),
    @Indexed
    val jobUuid: String,
) : JobInstanceParamDomain {
    override val job: JobInstanceDomain
        get() = jobInstance!!
}