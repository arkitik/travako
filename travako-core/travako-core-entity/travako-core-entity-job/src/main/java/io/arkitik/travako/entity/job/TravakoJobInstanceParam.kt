package io.arkitik.travako.entity.job

import io.arkitik.travako.core.domain.job.JobInstanceParamDomain
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:10 PM, 26/08/2024
 */
@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(
            name = "travako_job_instance_param_key_unique",
            columnNames = [
                "job_uuid",
                "key",
            ]
        )
    ]
)
class TravakoJobInstanceParam(
    @Id
    @Column(nullable = false, updatable = false)
    override val uuid: String,
    @ManyToOne(optional = false)
    @JoinColumn(
        foreignKey = ForeignKey(
            name = "travako_job_instance_param_job_fk"
        )
    )
    override val job: TravakoJobInstance,
    @Column(nullable = false, updatable = false)
    override val key: String,
    @Column
    override var value: String?,
    @Column(nullable = false, updatable = false)
    override val creationDate: LocalDateTime = LocalDateTime.now(),
) : JobInstanceParamDomain
