package io.arkitik.travako.entity.job

import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.server.TravakoServer
import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:19 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(
            name = "travako_job_instance_key_server_unique",
            columnNames = [
                "jobKey", "serverUuid"
            ]
        )
    ]
)
data class TravakoJobInstance(
    @Column(nullable = false, updatable = false)
    override val jobKey: String,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    override var jobStatus: JobStatus,
    @Id
    override val uuid: String,
    @ManyToOne(optional = false)
    @JoinColumn(
        foreignKey = ForeignKey(name = "travako_job_instance_server_fk"),
        updatable = false,
        name = "serverUuid"
    )
    override val server: TravakoServer,
    @Column(nullable = false)
    override var jobTrigger: String,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    override var jobTriggerType: JobInstanceTriggerType,
    @ManyToOne
    @JoinColumn(
        foreignKey = ForeignKey(name = "travako_job_instance_runner_fk"),
        name = "runnerUuid"
    )
    override var assignedTo: TravakoSchedulerRunner? = null,
    @Column(nullable = false, updatable = false)
    override val creationDate: LocalDateTime = LocalDateTime.now(),
    override var lastRunningTime: LocalDateTime? = null,
) : JobInstanceDomain
