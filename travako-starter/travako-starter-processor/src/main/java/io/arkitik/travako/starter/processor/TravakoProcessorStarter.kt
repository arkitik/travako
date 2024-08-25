package io.arkitik.travako.starter.processor

import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.function.processor.PreProcessor
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.sdk.leader.LeaderSdk
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.sdk.server.ServerSdk
import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.job.bean.JobInstanceRestartProcessor
import io.arkitik.travako.starter.job.source.JobInstancesSource
import io.arkitik.travako.starter.processor.config.TravakoConfig
import io.arkitik.travako.starter.processor.function.JobInstancesSourceImpl
import io.arkitik.travako.starter.processor.function.TravakoStartupInitializerProcessor
import io.arkitik.travako.starter.processor.function.TravakoStartupRunnerProcessor
import io.arkitik.travako.starter.processor.job.JobInstanceRestartProcessorImpl
import io.arkitik.travako.starter.processor.job.JobInstancesProcessor
import io.arkitik.travako.starter.processor.job.JobsSchedulerRegistry
import io.arkitik.travako.starter.processor.leader.LeaderJobsAssigneeProcessor
import io.arkitik.travako.starter.processor.leader.LeaderRegistrationProcess
import io.arkitik.travako.starter.processor.leader.LeaderRunnersAvailabilityProcessor
import io.arkitik.travako.starter.processor.leader.SwitchLeaderProcessor
import io.arkitik.travako.starter.processor.runner.RunnerHeartbeatProcess
import io.arkitik.travako.starter.processor.runner.RunnerJobExecutor
import io.arkitik.travako.starter.processor.runner.RunnerJobRestartProcessor
import io.arkitik.travako.starter.processor.runner.RunnerJobsRegisterProcessor
import io.arkitik.travako.starter.processor.runner.SchedulerRunnerRegistrationProcess
import io.arkitik.travako.starter.processor.runner.ShutdownTrigger
import io.arkitik.travako.starter.processor.server.ServerRegistrationProcess
import io.arkitik.travako.starter.processor.units.SpringJobSourceUnit
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
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
    fun travakoStartupInitializerProcessor(
        processors: List<Processor<*>>,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
    ): InitializingBean =
        TravakoStartupInitializerProcessor(processors, travakoTransactionalExecutor)

    @Bean
    fun travakoStartupRunnerProcessor(
        processors: List<Processor<*>>,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
    ): CommandLineRunner = TravakoStartupRunnerProcessor(processors, travakoTransactionalExecutor)

    @Bean
    fun leaderRegistrationProcess(
        travakoConfig: TravakoConfig,
        leaderSdk: LeaderSdk,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
    ): PreProcessor<LeaderDomain> = LeaderRegistrationProcess(
        travakoConfig = travakoConfig,
        leaderSdk = leaderSdk,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
    )

    @Bean
    fun switchLeaderProcessor(
        travakoConfig: TravakoConfig,
        leaderSdk: LeaderSdk,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
        taskScheduler: TaskScheduler,
    ): Processor<LeaderDomain> = SwitchLeaderProcessor(
        travakoConfig = travakoConfig,
        taskScheduler = taskScheduler,
        leaderSdk = leaderSdk,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
    )

    @Bean
    fun leaderJobsAssigneeProcessor(
        travakoConfig: TravakoConfig,
        taskScheduler: TaskScheduler,
        jobInstanceSdk: JobInstanceSdk,
        schedulerRunnerSdk: SchedulerRunnerSdk,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
        leaderSdk: LeaderSdk,
    ): Processor<LeaderDomain> = LeaderJobsAssigneeProcessor(
        travakoConfig = travakoConfig,
        taskScheduler = taskScheduler,
        jobInstanceSdk = jobInstanceSdk,
        schedulerRunnerSdk = schedulerRunnerSdk,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
        leaderSdk = leaderSdk,
    )

    @Bean
    fun runnersAvailabilityVerifyingProcessor(
        travakoConfig: TravakoConfig,
        taskScheduler: TaskScheduler,
        schedulerRunnerSdk: SchedulerRunnerSdk,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
        leaderSdk: LeaderSdk,
    ): Processor<LeaderDomain> = LeaderRunnersAvailabilityProcessor(
        travakoConfig = travakoConfig,
        taskScheduler = taskScheduler,
        schedulerRunnerSdk = schedulerRunnerSdk,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
        leaderSdk = leaderSdk
    )

    @Bean
    fun jobInstancesProcessor(
        travakoConfig: TravakoConfig,
        jobInstancesSource: JobInstancesSource,
        jobInstanceSdk: JobInstanceSdk,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
    ): Processor<JobInstanceDomain> = JobInstancesProcessor(
        travakoConfig = travakoConfig,
        jobInstanceSdk = jobInstanceSdk,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
        jobInstancesSource = jobInstancesSource
    )

    @Bean
    fun schedulerRunnerRegistrationProcess(
        travakoConfig: TravakoConfig,
        schedulerRunnerSdk: SchedulerRunnerSdk,
        applicationContext: ApplicationContext,
    ): PreProcessor<SchedulerRunnerDomain> = SchedulerRunnerRegistrationProcess(
        travakoConfig = travakoConfig,
        schedulerRunnerSdk = schedulerRunnerSdk,
        applicationContext = applicationContext
    )

    @Bean
    fun runnerHeartbeatProcess(
        travakoConfig: TravakoConfig,
        schedulerRunnerSdk: SchedulerRunnerSdk,
        taskScheduler: TaskScheduler,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
    ): Processor<SchedulerRunnerDomain> = RunnerHeartbeatProcess(
        travakoConfig = travakoConfig,
        schedulerRunnerSdk = schedulerRunnerSdk,
        taskScheduler = taskScheduler,
        travakoTransactionalExecutor = travakoTransactionalExecutor
    )

    @Bean
    fun runnerJobsRegisterProcessor(
        jobInstancesSource: JobInstancesSource,
        jobsSchedulerRegistry: JobsSchedulerRegistry,
    ): Processor<SchedulerRunnerDomain> = RunnerJobsRegisterProcessor(
        jobInstancesSource = jobInstancesSource,
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
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
        jobInstanceSdk: JobInstanceSdk,
    ) = RunnerJobExecutor(
        travakoConfig = travakoConfig,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
        jobInstanceSdk = jobInstanceSdk,
    )

    @Bean
    fun runnerJobRestartProcessor(
        travakoConfig: TravakoConfig,
        taskScheduler: TaskScheduler,
        jobEventSdk: JobEventSdk,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
        jobInstancesSource: JobInstancesSource,
        jobsSchedulerRegistry: JobsSchedulerRegistry,
    ): Processor<SchedulerRunnerDomain> = RunnerJobRestartProcessor(
        travakoConfig = travakoConfig,
        taskScheduler = taskScheduler,
        jobEventSdk = jobEventSdk,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
        jobsSchedulerRegistry = jobsSchedulerRegistry,
        jobInstancesSource = jobInstancesSource
    )

    @Bean
    fun serverRegistrationProcess(
        travakoConfig: TravakoConfig,
        serverSdk: ServerSdk,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
    ): PreProcessor<ServerDomain> = ServerRegistrationProcess(
        travakoConfig = travakoConfig,
        serverSdk = serverSdk,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
    )

    @Bean
    fun jobInstanceRestartProcessor(
        travakoConfig: TravakoConfig,
        jobInstanceSdk: JobInstanceSdk,
    ): JobInstanceRestartProcessor =
        JobInstanceRestartProcessorImpl(
            travakoConfig = travakoConfig,
            jobInstanceSdk = jobInstanceSdk,
        )

    @Bean
    fun shutdownTrigger(
        travakoConfig: TravakoConfig,
        schedulerRunnerSdk: SchedulerRunnerSdk,
    ): DisposableBean = ShutdownTrigger(
        travakoConfig = travakoConfig,
        schedulerRunnerSdk = schedulerRunnerSdk
    )

    @Bean
    fun jobInstancesSource(
        sourceUnits: List<JobInstancesSource.SourceUnit>,
    ): JobInstancesSource = JobInstancesSourceImpl(
        sourceUnits = sourceUnits
    )

    @Bean
    fun springJobInstancesSourceUnit(
        jobInstanceBeans: List<JobInstanceBean>,
    ): JobInstancesSource.SourceUnit =
        SpringJobSourceUnit(jobInstanceBeans)
}
