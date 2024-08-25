package io.arkitik.travako.port.shared

import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.port.shared.function.TravakoTransactionalExecutorImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:32 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class SharedPortContext {
    @Bean
    fun travakoTransactionalExecutor(
        transactionManager: PlatformTransactionManager,
    ): TravakoTransactionalExecutor = TravakoTransactionalExecutorImpl(transactionManager)
}
