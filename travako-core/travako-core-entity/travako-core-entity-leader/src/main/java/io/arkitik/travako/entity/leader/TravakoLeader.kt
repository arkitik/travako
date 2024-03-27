package io.arkitik.travako.entity.leader

import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.server.TravakoServer
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

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
