package io.arkitik.travako.starter.processor

import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.function.processor.PreProcessor
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.sdk.leader.LeaderSdk
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.sdk.server.ServerSdk
import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.job.bean.JobInstanceRestartProcessor
import io.arkitik.travako.starter.processor.config.TravakoConfig
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
        transactionalExecutor: TransactionalExecutor,
    ): InitializingBean =
        TravakoStartupInitializerProcessor(processors, transactionalExecutor)

    @Bean
    fun travakoStartupRunnerProcessor(
        processors: List<Processor<*>>,
        transactionalExecutor: TransactionalExecutor,
    ): CommandLineRunner = TravakoStartupRunnerProcessor(processors, transactionalExecutor)

    @Bean
    fun leaderRegistrationProcess(
        travakoConfig: TravakoConfig,
        leaderSdk: LeaderSdk,
        transactionalExecutor: TransactionalExecutor,
        jobInstances: List<JobInstanceBean>,
        taskScheduler: TaskScheduler,
        jobInstanceSdk: JobInstanceSdk,
    ): PreProcessor<LeaderDomain> = LeaderRegistrationProcess(
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
    ): Processor<LeaderDomain> = SwitchLeaderProcessor(
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
    ): Processor<LeaderDomain> = LeaderJobsAssigneeProcessor(
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
    ): Processor<LeaderDomain> = LeaderRunnersAvailabilityProcessor(
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
    ): Processor<JobInstanceDomain> = JobInstancesProcessor(
        travakoConfig = travakoConfig,
        jobInstances = jobInstances,
        jobInstanceSdk = jobInstanceSdk,
        transactionalExecutor = transactionalExecutor
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
        transactionalExecutor: TransactionalExecutor,
    ): Processor<SchedulerRunnerDomain> = RunnerHeartbeatProcess(
        travakoConfig = travakoConfig,
        schedulerRunnerSdk = schedulerRunnerSdk,
        taskScheduler = taskScheduler,
        transactionalExecutor = transactionalExecutor
    )

    @Bean
    fun runnerJobsRegisterProcessor(
        jobInstances: List<JobInstanceBean>,
        jobsSchedulerRegistry: JobsSchedulerRegistry,
    ): Processor<SchedulerRunnerDomain> = RunnerJobsRegisterProcessor(
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
    ): Processor<SchedulerRunnerDomain> = RunnerJobRestartProcessor(
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
    ): PreProcessor<ServerDomain> = ServerRegistrationProcess(
        travakoConfig = travakoConfig,
        serverSdk = serverSdk,
        transactionalExecutor = transactionalExecutor,
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
}
