package io.arkitik.travako.entity.redis.job.event

import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.embedded.JobEventType
import io.arkitik.travako.entity.redis.job.TravakoJobInstance
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Reference
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 03 11:29 PM, **Mon, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@RedisHash("travako:job:event")
data class TravakoJobEvent(
    @Id
    override val uuid: String,
    @Reference
    var travakoJobInstance: TravakoJobInstance?,
    override val eventType: JobEventType,
    override var processedFlag: Boolean = false,
    override val creationDate: LocalDateTime = LocalDateTime.now(),
    @Indexed
    val serverUuid: String,
) : JobEventDomain {
    override val jobInstance: JobInstanceDomain
        get() = travakoJobInstance!!
}
