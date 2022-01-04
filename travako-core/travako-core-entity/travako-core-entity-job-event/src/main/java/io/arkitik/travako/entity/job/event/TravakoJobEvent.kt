package io.arkitik.travako.entity.job.event

import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.embedded.JobEventType
import io.arkitik.travako.entity.job.TravakoJobInstance
import java.time.LocalDateTime
import javax.persistence.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 03 11:29 PM, **Mon, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Entity
data class TravakoJobEvent(
    @Id
    override val uuid: String,
    @ManyToOne(optional = false)
    @JoinColumn(
        foreignKey = ForeignKey(name = "travako_job_event_instance_fk"),
        name = "jobUuid"
    )
    override var jobInstance: TravakoJobInstance,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    override val eventType: JobEventType,
    @Column(nullable = false)
    override var processedFlag: Boolean = false,
    @Column(nullable = false, updatable = false)
    override val creationDate: LocalDateTime = LocalDateTime.now(),
) : JobEventDomain
