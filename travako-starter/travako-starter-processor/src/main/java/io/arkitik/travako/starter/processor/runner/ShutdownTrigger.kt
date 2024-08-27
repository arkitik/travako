package io.arkitik.travako.starter.processor.runner

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.starter.processor.config.TravakoRunnerConfig
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import org.springframework.beans.factory.DisposableBean

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 09 9:11 PM, **Wed, February 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class ShutdownTrigger(
    private val travakoConfig: TravakoConfig,
    private val travakoRunnerConfig: TravakoRunnerConfig,
    private val schedulerRunnerSdk: SchedulerRunnerSdk,
) : DisposableBean {
    override fun destroy() {
        schedulerRunnerSdk.markRunnerAsDown
            .runOperation(
                RunnerKeyDto(
                    serverKey = travakoConfig.serverKey,
                    runnerKey = travakoRunnerConfig.key,
                    runnerHost = travakoRunnerConfig.host,
                )
            )
    }
}
