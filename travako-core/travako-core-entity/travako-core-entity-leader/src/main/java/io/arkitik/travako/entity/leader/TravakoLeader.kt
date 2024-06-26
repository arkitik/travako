package io.arkitik.travako.entity.leader

import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.server.TravakoServer
import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 8:00 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(
            name = "travako_leader_server_unique",
            columnNames = [
                "serverUuid"
            ]
        )
    ]
)
data class TravakoLeader(
    @Id
    override val uuid: String,
    @ManyToOne
    @JoinColumn(
        foreignKey = ForeignKey(name = "travako_leader_runner_fk"),
        name = "runnerUuid"
    )
    override var runner: TravakoSchedulerRunner,
    @ManyToOne(optional = false)
    @JoinColumn(
        foreignKey = ForeignKey(name = "travako_leader_server_fk"),
        updatable = false,
        name = "serverUuid"
    )
    override val server: TravakoServer,
    @Column(nullable = false)
    override var lastModifiedDate: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = false, updatable = false)
    override val creationDate: LocalDateTime = LocalDateTime.now(),
) : LeaderDomain
