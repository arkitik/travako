package io.arkitik.travako.starter.processor

import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.sdk.leader.LeaderSdk
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.sdk.server.ServerSdk
import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.processor.config.TravakoConfig
import io.arkitik.travako.starter.processor.function.TravakoStartupProcessor
import io.arkitik.travako.starter.processor.job.JobInstancesProcessor
import io.arkitik.travako.starter.processor.job.JobsSchedulerRegistry
import io.arkitik.travako.starter.processor.leader.LeaderJobsAssigneeProcessor
import io.arkitik.travako.starter.processor.leader.LeaderRegistrationProcess
import io.arkitik.travako.starter.processor.leader.LeaderRunnersAvailabilityProcessor
import io.arkitik.travako.starter.processor.leader.SwitchLeaderProcessor
import io.arkitik.travako.starter.processor.runner.*
import io.arkitik.travako.starter.processor.server.ServerRegistrationProcess
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 10:29 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
@EnableConfigurationProperties(TravakoConfig::class)
class TravakoProcessorStarter {
    @Bean
    fun travakoStartupProcessor(
        processors: List<Processor<*>>,
        transactionalExecutor: TransactionalExecutor,
    ) = TravakoStartupProcessor(
        processors,
        transactionalExecutor
    )

    @Bean
    fun leaderRegistrationProcess(
        travakoConfig: TravakoConfig,
        leaderSdk: LeaderSdk,
        transactionalExecutor: TransactionalExecutor,
        jobInstances: List<JobInstanceBean>,
        taskScheduler: TaskScheduler,
        jobInstanceSdk: JobInstanceSdk,
    ) = LeaderRegistrationProcess(
        travakoConfig = travakoConfig,
        leaderSdk = leaderSdk,
        transactionalExecutor = transactionalExecutor,
    )

    @Bean
    fun switchLeaderProcessor(
        travakoConfig: TravakoConfig,
        leaderSdk: LeaderSdk,
        transactionalExecutor: TransactionalExecutor,
        taskScheduler: TaskScheduler,
    ) = SwitchLeaderProcessor(
        travakoConfig = travakoConfig,
        taskScheduler = taskScheduler,
        leaderSdk = leaderSdk,
        transactionalExecutor = transactionalExecutor,
    )

    @Bean
    fun leaderJobsAssigneeProcessor(
        travakoConfig: TravakoConfig,
        taskScheduler: TaskScheduler,
        jobInstanceSdk: JobInstanceSdk,
        schedulerRunnerSdk: SchedulerRunnerSdk,
        transactionalExecutor: TransactionalExecutor,
        leaderSdk: LeaderSdk,
    ) = LeaderJobsAssigneeProcessor(
        travakoConfig = travakoConfig,
        taskScheduler = taskScheduler,
        jobInstanceSdk = jobInstanceSdk,
        schedulerRunnerSdk = schedulerRunnerSdk,
        transactionalExecutor = transactionalExecutor,
        leaderSdk = leaderSdk,
    )

    @Bean
    fun runnersAvailabilityVerifyingProcessor(
        travakoConfig: TravakoConfig,
        taskScheduler: TaskScheduler,
        jobInstanceSdk: JobInstanceSdk,
        schedulerRunnerSdk: SchedulerRunnerSdk,
        transactionalExecutor: TransactionalExecutor,
        leaderSdk: LeaderSdk,
    ) = LeaderRunnersAvailabilityProcessor(
        travakoConfig = travakoConfig,
        taskScheduler = taskScheduler,
        schedulerRunnerSdk = schedulerRunnerSdk,
        transactionalExecutor = transactionalExecutor,
        leaderSdk = leaderSdk
    )

    @Bean
    fun jobInstancesProcessor(
        travakoConfig: TravakoConfig,
        jobInstances: List<JobInstanceBean>,
        jobInstanceSdk: JobInstanceSdk,
        transactionalExecutor: TransactionalExecutor,
    ) = JobInstancesProcessor(
        travakoConfig = travakoConfig,
        jobInstances = jobInstances,
        jobInstanceSdk = jobInstanceSdk,
        transactionalExecutor = transactionalExecutor
    )

    @Bean
    fun schedulerRunnerRegistrationProcess(
        travakoConfig: TravakoConfig,
        schedulerRunnerSdk: SchedulerRunnerSdk,
        transactionalExecutor: TransactionalExecutor,
    ) = SchedulerRunnerRegistrationProcess(
        travakoConfig = travakoConfig,
        schedulerRunnerSdk = schedulerRunnerSdk,
        transactionalExecutor = transactionalExecutor,
    )

    @Bean
    fun runnerHeartbeatProcess(
        travakoConfig: TravakoConfig,
        schedulerRunnerSdk: SchedulerRunnerSdk,
        taskScheduler: TaskScheduler,
        transactionalExecutor: TransactionalExecutor,
    ) = RunnerHeartbeatProcess(
        travakoConfig = travakoConfig,
        schedulerRunnerSdk = schedulerRunnerSdk,
        taskScheduler = taskScheduler,
        transactionalExecutor = transactionalExecutor
    )

    @Bean
    fun runnerJobsRegisterProcessor(
        jobInstances: List<JobInstanceBean>,
        jobsSchedulerRegistry: JobsSchedulerRegistry,
    ) = RunnerJobsRegisterProcessor(
        jobInstances = jobInstances,
        jobsSchedulerRegistry = jobsSchedulerRegistry
    )

    @Bean
    fun jobsSchedulerRegistry(
        taskScheduler: TaskScheduler,
        runnerJobExecutor: RunnerJobExecutor,
    ) = JobsSchedulerRegistry(
        taskScheduler = taskScheduler,
        runnerJobExecutor = runnerJobExecutor
    )

    @Bean
    fun runnerJobExecutor(
        travakoConfig: TravakoConfig,
        transactionalExecutor: TransactionalExecutor,
        jobInstanceSdk: JobInstanceSdk,
    ) = RunnerJobExecutor(
        travakoConfig = travakoConfig,
        transactionalExecutor = transactionalExecutor,
        jobInstanceSdk = jobInstanceSdk,
    )

    @Bean
    fun runnerJobRestartProcessor(
        travakoConfig: TravakoConfig,
        taskScheduler: TaskScheduler,
        jobEventSdk: JobEventSdk,
        transactionalExecutor: TransactionalExecutor,
        jobInstances: List<JobInstanceBean>,
        jobsSchedulerRegistry: JobsSchedulerRegistry,
    ) = RunnerJobRestartProcessor(
        travakoConfig = travakoConfig,
        taskScheduler = taskScheduler,
        jobEventSdk = jobEventSdk,
        transactionalExecutor = transactionalExecutor,
        jobInstances = jobInstances,
        jobsSchedulerRegistry = jobsSchedulerRegistry,
    )

    @Bean
    fun serverRegistrationProcess(
        travakoConfig: TravakoConfig,
        serverSdk: ServerSdk,
        transactionalExecutor: TransactionalExecutor,
    ) = ServerRegistrationProcess(
        travakoConfig = travakoConfig,
        serverSdk = serverSdk,
        transactionalExecutor = transactionalExecutor,
    )
}
