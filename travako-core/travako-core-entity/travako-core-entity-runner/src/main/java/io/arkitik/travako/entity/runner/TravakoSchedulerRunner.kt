package io.arkitik.travako.entity.runner

import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
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
 * Created At 25 8:06 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(
            name = "travako_scheduler_runner_key_server_unique",
            columnNames = [
                "runnerKey", "serverUuid", "runnerHost"
            ]
        )
    ]
)
data class TravakoSchedulerRunner(
    @Column(nullable = false, updatable = false)
    override val runnerKey: String,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    override var instanceState: InstanceState,
    @Column(nullable = false)
    override val runnerHost: String,
    @Id
    override val uuid: String,
    @ManyToOne(optional = false)
    @JoinColumn(
        foreignKey = ForeignKey(name = "travako_scheduler_runner_server_fk"),
        updatable = false,
        name = "serverUuid"
    )
    override val server: TravakoServer,
    @Column(nullable = false, updatable = false)
    override val creationDate: LocalDateTime = LocalDateTime.now(),
    @Column
    override var lastHeartbeatTime: LocalDateTime? = null,
) : SchedulerRunnerDomain
