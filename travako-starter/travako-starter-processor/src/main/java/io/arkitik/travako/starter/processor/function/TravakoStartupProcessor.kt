package io.arkitik.travako.starter.processor.function

import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.function.processor.PreProcessor
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.CommandLineRunner

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 10:31 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class TravakoStartupProcessor(
    private val registeredProcessors: List<Processor<*>>,
    private val transactionalExecutor: TransactionalExecutor,
) : CommandLineRunner, InitializingBean {
    override fun run(vararg args: String?) {
        transactionalExecutor.runOnTransaction {
            val processors = registeredProcessors.filter {
                it !is PreProcessor
            }
            runProcessors(processors)
        }
    }

    override fun afterPropertiesSet() {
        transactionalExecutor.runOnTransaction {
            val processors = registeredProcessors.filterIsInstance<PreProcessor<*>>()
            runProcessors(processors)
        }
    }

    private fun runProcessors(processors: List<Processor<*>>) {
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
