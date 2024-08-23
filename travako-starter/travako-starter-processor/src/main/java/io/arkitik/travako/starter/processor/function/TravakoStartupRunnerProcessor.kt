package io.arkitik.travako.starter.processor.function

import io.arkitik.travako.function.processor.PreProcessor
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import org.springframework.boot.CommandLineRunner

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 10:31 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class TravakoStartupRunnerProcessor(
    private val registeredProcessors: List<Processor<*>>,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
) : TravakoStartupProcessor(), CommandLineRunner {
    override fun run(vararg args: String?) {
        travakoTransactionalExecutor.runUnitTransaction {
            val processors = registeredProcessors.filter {
                it !is PreProcessor
            }
            runProcessors(processors)
        }
    }
}
