package io.arkitik.travako.adapter.exposed.job.query

import io.arkitik.radix.adapter.exposed.query.ExposedStoreQuery
import io.arkitik.radix.develop.exposed.table.ensureInTransaction
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.exposed.job.TravakoJobInstanceTable
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:19 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class JobInstanceStoreQueryImpl(
    database: Database?,
    identityTable: TravakoJobInstanceTable,
) : ExposedStoreQuery<String, JobInstanceDomain, TravakoJobInstanceTable>(
    identityTable = identityTable,
    database = database
), JobInstanceStoreQuery {
    override fun findByServerAndJobKey(
        server: ServerDomain,
        jobKey: String,
    ): JobInstanceDomain? {
        return ensureInTransaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server.eq(server.uuid))
                        .and(identityTable.jobKey.eq(jobKey))
                }.limit(1).singleOrNull()?.let { identityTable.mapToIdentity(it, database) }
        }
    }

    override fun existsByServerAndAssignedToRunnerAndJobKey(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        jobKey: String,
    ): Boolean {
        return ensureInTransaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server.eq(server.uuid))
                        .and(identityTable.jobKey.eq(jobKey))
                        .and(identityTable.assignedTo.eq(runner.uuid))
                }.exist()
        }
    }

    override fun findAllByServerAndRunnerAndStatusNextExecutionTimeIsBefore(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        status: JobStatus,
        nextExecutionTime: LocalDateTime,
    ): List<JobInstanceDomain> {
        return transaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server.eq(server.uuid))
                        .and(identityTable.assignedTo.eq(runner.uuid))
                        .and(identityTable.jobStatus.eq(status))
                        .and(identityTable.nextExecutionTime.less(nextExecutionTime))
                }.orderBy(identityTable.nextExecutionTime, SortOrder.ASC)
                .map {
                    identityTable.mapToIdentity(it, database)
                }
        }
    }

    override fun findByServerAndJobKeyAndStatus(
        server: ServerDomain,
        jobKey: String,
        status: JobStatus,
    ): JobInstanceDomain? {
        return ensureInTransaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server.eq(server.uuid))
                        .and(identityTable.jobKey.eq(jobKey))
                        .and(identityTable.jobStatus eq status)
                }.limit(1).singleOrNull()?.let { identityTable.mapToIdentity(it, database) }
        }
    }

    override fun findAllByServerAndStatusIn(server: ServerDomain, statuses: List<JobStatus>): List<JobInstanceDomain> =
        transaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server.eq(server.uuid))
                        .and(identityTable.jobStatus.inList(statuses))
                }.map { identityTable.mapToIdentity(it, database) }
        }

    override fun findAllByServerAndRunner(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
    ): List<JobInstanceDomain> =
        transaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server.eq(server.uuid))
                        .and(identityTable.assignedTo.eq(runner.uuid))
                }.map { identityTable.mapToIdentity(it, database) }
        }

    override fun existsByServerAndJobKeyAndStatusIn(
        server: ServerDomain,
        jobKey: String,
        statuses: List<JobStatus>,
    ): Boolean =
        transaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server.eq(server.uuid))
                        .and(identityTable.jobKey.eq(jobKey))
                        .and(identityTable.jobStatus.inList(statuses))
                }.exist()
        }

    override fun existsByServerAndJobKeys(server: ServerDomain, jobKeys: List<String>): Boolean =
        transaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server.eq(server.uuid))
                        .and(identityTable.jobKey.inList(jobKeys))
                }.exist()
        }

    override fun findAllByServerAndJobKeys(server: ServerDomain, jobKeys: List<String>): List<JobInstanceDomain> =
        transaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server.eq(server.uuid))
                        .and(identityTable.jobKey.inList(jobKeys))
                }.map { identityTable.mapToIdentity(it, database) }
        }

    override fun findAllByServerAndRunnerStateIs(
        server: ServerDomain,
        instanceState: InstanceState,
    ): List<JobInstanceDomain> {
        return transaction(database) {
            val runnerTable = identityTable.runnerTable
            identityTable
                .innerJoin(runnerTable)
                .selectAll()
                .where {
                    (identityTable.server.eq(server.uuid))
                        .and(runnerTable.instanceState.eq(instanceState))
                }.map { identityTable.mapToIdentity(it, database) }
        }
    }
}
