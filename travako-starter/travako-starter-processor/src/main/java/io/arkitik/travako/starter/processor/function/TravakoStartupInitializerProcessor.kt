package io.arkitik.travako.starter.processor.function

import io.arkitik.travako.function.processor.PreProcessor
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import org.springframework.beans.factory.InitializingBean

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 10:31 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class TravakoStartupInitializerProcessor(
    private val registeredProcessors: List<Processor<*>>,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
) : TravakoStartupProcessor(), InitializingBean {
    override fun afterPropertiesSet() {
        travakoTransactionalExecutor.runUnitTransaction {
            val processors = registeredProcessors.filterIsInstance<PreProcessor<*>>()
            runProcessors(processors)
        }
    }
}
