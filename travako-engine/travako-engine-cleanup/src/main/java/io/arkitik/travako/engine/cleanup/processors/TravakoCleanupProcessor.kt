package io.arkitik.travako.engine.cleanup.processors

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.operation.ext.runOperator
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.engine.cleanup.config.TravakoCleanupConfig
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.sdk.domain.job.JobDomainSdk
import io.arkitik.travako.sdk.domain.job.dto.JobCleanupDto
import io.arkitik.travako.sdk.domain.job.event.JobEventDomainSdk
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import io.arkitik.travako.starter.processor.core.logger.logger
import org.springframework.scheduling.TaskScheduler
import java.time.Duration
import java.time.LocalDate

/**
 * @author Ibrahim Al-Tamimi 
 * @since 11:20, Tuesday, 31/03/2026
 **/
internal class TravakoCleanupProcessor(
    private val serverDomainSdk: ServerDomainSdk,
    private val jobDomainSdk: JobDomainSdk,
    private val jobDomainEventSdk: JobEventDomainSdk,
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
    private val taskScheduler: TaskScheduler,
    private val travakoConfig: TravakoConfig,
    private val travakoCleanupConfig: TravakoCleanupConfig,
) : Processor<JobInstanceDomain> {
    companion object {
        private val logger = logger<TravakoCleanupProcessor>()
    }

    override val type: Class<JobInstanceDomain> = JobInstanceDomain::class.java

    override fun process() {
        taskScheduler.scheduleAtFixedRate(::cleanup, Duration.ofHours(travakoCleanupConfig.runIntervalHours))
    }

    private fun cleanup() {
        val server = serverDomainSdk.fetchServer
            .runOperation(
                ServerDomainDto(
                    serverKey = travakoConfig.serverKey
                )
            )
        unassignJobsFromDownRunners(server)
        cleanEventStateForDownRunners(server)
        cleanProcessedJobEvents(server)
        cleanDoneJobs(server)
        cleanDownRunners(server)
    }

    private fun unassignJobsFromDownRunners(server: ServerDomain) {
        logger.debug("Starting unassignment of jobs from down runners for server ${server.serverKey}")
        jobDomainSdk.removeAssigneeFromDownRunners
            .runOperator(server, Unit)
    }

    private fun cleanEventStateForDownRunners(server: ServerDomain) {
        logger.debug("Cleaning up the job event states for down runners for server ${server.serverKey}")
        jobDomainEventSdk.cleanupJobEventStatesForDownRunners
            .runOperator(
                server,
                Unit
            )
    }

    private fun cleanProcessedJobEvents(server: ServerDomain) {
        logger.debug("Cleaning up the processed job events for server ${server.serverKey}")
        jobDomainEventSdk.cleanupDoneJobEvents
            .runOperator(
                server,
                Unit
            )
    }

    private fun cleanDoneJobs(server: ServerDomain) {
        logger.debug("Cleaning up the done job instances older than ${travakoCleanupConfig.jobsAgeInDays} days for server ${server.serverKey}")
        jobDomainSdk.cleanupDoneJobInstances
            .runOperator(
                JobCleanupDto(
                    beforeDate = LocalDate.now().minusDays(travakoCleanupConfig.jobsAgeInDays),
                    server = server
                ),
                Unit
            )
    }

    private fun cleanDownRunners(server: ServerDomain) {
        logger.debug("Cleaning up the down runners for server ${server.serverKey}")
        schedulerRunnerDomainSdk.cleanupDownRunners
            .runOperator(server, Unit)
    }
}