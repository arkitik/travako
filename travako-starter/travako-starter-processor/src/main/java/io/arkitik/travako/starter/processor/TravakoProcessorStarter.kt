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
import io.arkitik.travako.starter.job.registry.JobInstancesRegistry
import io.arkitik.travako.starter.job.source.JobInstancesSource
import io.arkitik.travako.starter.job.source.TravakoJobInstanceProvider
import io.arkitik.travako.starter.processor.config.TravakoLeaderConfig
import io.arkitik.travako.starter.processor.config.TravakoRunnerConfig
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
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
import io.arkitik.travako.starter.processor.runner.RunnerJobEventsProcessor
import io.arkitik.travako.starter.processor.runner.RunnerJobExecutor
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
@EnableConfigurationProperties(
    TravakoLeaderConfig::class,
    TravakoRunnerConfig::class
)
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
        travakoRunnerConfig: TravakoRunnerConfig,
    ): PreProcessor<LeaderDomain> = LeaderRegistrationProcess(
        travakoConfig = travakoConfig,
        leaderSdk = leaderSdk,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
        travakoRunnerConfig = travakoRunnerConfig
    )

    @Bean
    fun switchLeaderProcessor(
        travakoConfig: TravakoConfig,
        leaderSdk: LeaderSdk,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
        taskScheduler: TaskScheduler,
        travakoLeaderConfig: TravakoLeaderConfig,
        travakoRunnerConfig: TravakoRunnerConfig,
    ): Processor<LeaderDomain> = SwitchLeaderProcessor(
        travakoConfig = travakoConfig,
        taskScheduler = taskScheduler,
        leaderSdk = leaderSdk,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
        travakoLeaderConfig = travakoLeaderConfig,
        travakoRunnerConfig = travakoRunnerConfig
    )

    @Bean
    fun leaderJobsAssigneeProcessor(
        travakoConfig: TravakoConfig,
        taskScheduler: TaskScheduler,
        jobInstanceSdk: JobInstanceSdk,
        schedulerRunnerSdk: SchedulerRunnerSdk,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
        leaderSdk: LeaderSdk,
        travakoLeaderConfig: TravakoLeaderConfig,
        travakoRunnerConfig: TravakoRunnerConfig,
    ): Processor<LeaderDomain> = LeaderJobsAssigneeProcessor(
        travakoConfig = travakoConfig,
        taskScheduler = taskScheduler,
        jobInstanceSdk = jobInstanceSdk,
        schedulerRunnerSdk = schedulerRunnerSdk,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
        leaderSdk = leaderSdk,
        travakoLeaderConfig = travakoLeaderConfig,
        travakoRunnerConfig = travakoRunnerConfig,
    )

    @Bean
    fun runnersAvailabilityVerifyingProcessor(
        travakoConfig: TravakoConfig,
        taskScheduler: TaskScheduler,
        schedulerRunnerSdk: SchedulerRunnerSdk,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
        leaderSdk: LeaderSdk,
        travakoRunnerConfig: TravakoRunnerConfig,
    ): Processor<LeaderDomain> = LeaderRunnersAvailabilityProcessor(
        travakoConfig = travakoConfig,
        taskScheduler = taskScheduler,
        schedulerRunnerSdk = schedulerRunnerSdk,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
        leaderSdk = leaderSdk,
        travakoRunnerConfig = travakoRunnerConfig,
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
        travakoRunnerConfig: TravakoRunnerConfig,
    ): PreProcessor<SchedulerRunnerDomain> = SchedulerRunnerRegistrationProcess(
        travakoConfig = travakoConfig,
        schedulerRunnerSdk = schedulerRunnerSdk,
        applicationContext = applicationContext,
        travakoRunnerConfig = travakoRunnerConfig
    )

    @Bean
    fun runnerHeartbeatProcess(
        travakoConfig: TravakoConfig,
        schedulerRunnerSdk: SchedulerRunnerSdk,
        taskScheduler: TaskScheduler,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
        travakoRunnerConfig: TravakoRunnerConfig,
    ): Processor<SchedulerRunnerDomain> = RunnerHeartbeatProcess(
        travakoConfig = travakoConfig,
        schedulerRunnerSdk = schedulerRunnerSdk,
        taskScheduler = taskScheduler,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
        travakoRunnerConfig = travakoRunnerConfig,
    )

    @Bean
    fun runnerJobsRegisterProcessor(
        jobsSchedulerRegistry: JobsSchedulerRegistry,
        travakoConfig: TravakoConfig,
        jobInstanceSdk: JobInstanceSdk,
        travakoJobInstanceProvider: TravakoJobInstanceProvider,
        jobInstancesSource: JobInstancesSource,
    ): Processor<SchedulerRunnerDomain> = RunnerJobsRegisterProcessor(
        jobsSchedulerRegistry = jobsSchedulerRegistry,
        travakoConfig = travakoConfig,
        jobInstanceSdk = jobInstanceSdk,
        travakoJobInstanceProvider = travakoJobInstanceProvider,
        jobInstancesSource = jobInstancesSource,
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
        travakoRunnerConfig: TravakoRunnerConfig,
    ) = RunnerJobExecutor(
        travakoConfig = travakoConfig,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
        jobInstanceSdk = jobInstanceSdk,
        travakoRunnerConfig = travakoRunnerConfig
    )

    @Bean
    fun runnerJobEventsProcessor(
        travakoConfig: TravakoConfig,
        taskScheduler: TaskScheduler,
        jobEventSdk: JobEventSdk,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
        jobsSchedulerRegistry: JobsSchedulerRegistry,
        jobInstanceSdk: JobInstanceSdk,
        travakoJobInstanceProvider: TravakoJobInstanceProvider,
        travakoRunnerConfig: TravakoRunnerConfig,
    ): Processor<SchedulerRunnerDomain> = RunnerJobEventsProcessor(
        travakoConfig = travakoConfig,
        taskScheduler = taskScheduler,
        jobEventSdk = jobEventSdk,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
        jobsSchedulerRegistry = jobsSchedulerRegistry,
        jobInstanceSdk = jobInstanceSdk,
        travakoJobInstanceProvider = travakoJobInstanceProvider,
        travakoRunnerConfig = travakoRunnerConfig,
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
        jobInstancesRegistry: JobInstancesRegistry,
    ): JobInstanceRestartProcessor =
        JobInstanceRestartProcessorImpl(jobInstancesRegistry)

    @Bean
    fun shutdownTrigger(
        travakoConfig: TravakoConfig,
        schedulerRunnerSdk: SchedulerRunnerSdk,
        travakoRunnerConfig: TravakoRunnerConfig,
    ): DisposableBean = ShutdownTrigger(
        travakoConfig = travakoConfig,
        schedulerRunnerSdk = schedulerRunnerSdk,
        travakoRunnerConfig = travakoRunnerConfig
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
