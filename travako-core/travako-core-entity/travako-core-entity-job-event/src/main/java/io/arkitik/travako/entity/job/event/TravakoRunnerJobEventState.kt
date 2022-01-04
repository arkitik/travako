package io.arkitik.travako.entity.job.event

import io.arkitik.travako.domain.job.event.RunnerJobEventStateDomain
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import java.time.LocalDateTime
import javax.persistence.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 03 11:42 PM, **Mon, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(
            name = "travako_job_event_runner_processed_state_unique",
            columnNames = [
                "runnerUuid", "jobEventUuid"
            ]
        )
    ]
)
data class TravakoRunnerJobEventState(
    @Id
    override val uuid: String,
    @ManyToOne(optional = false)
    @JoinColumn(
        foreignKey = ForeignKey(name = "travako_runner_job_event_state_runner_fk"),
        name = "runnerUuid"
    )
    override val runner: TravakoSchedulerRunner,
    @ManyToOne(optional = false)
    @JoinColumn(
        foreignKey = ForeignKey(name = "travako_runner_job_event_state_event_fk"),
        name = "jobEventUuid"
    )
    override val jobEvent: TravakoJobEvent,
    override val creationDate: LocalDateTime = LocalDateTime.now(),
) : RunnerJobEventStateDomain
