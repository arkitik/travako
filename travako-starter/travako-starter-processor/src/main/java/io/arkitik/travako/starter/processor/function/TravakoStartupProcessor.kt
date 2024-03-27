package io.arkitik.travako.starter.processor.function

import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.function.processor.Processor

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 10:31 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal abstract class TravakoStartupProcessor {
    protected fun runProcessors(processors: List<Processor<*>>) {
        processors
            .filter { it.type == ServerDomain::class.java }
            .forEach { processor ->
                processor.process()
            }
        processors
            .filter { it.type == SchedulerRunnerDomain::class.java }
            .forEach { processor ->
                processor.process()
            }
        processors
            .filter { it.type == LeaderDomain::class.java }
            .forEach { processor ->
                processor.process()
            }
        processors
            .filter { it.type == JobInstanceDomain::class.java }
            .forEach { processor ->
                processor.process()
            }
    }
}
