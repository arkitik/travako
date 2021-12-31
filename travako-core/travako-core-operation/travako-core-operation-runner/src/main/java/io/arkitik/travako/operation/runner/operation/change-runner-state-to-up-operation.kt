package io.arkitik.travako.operation.runner.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.radix.develop.store.storeUpdater
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.operation.runner.errors.RunnerErrors
import io.arkitik.travako.operation.runner.roles.CheckRegisteredRole
import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import io.arkitik.travako.store.runner.SchedulerRunnerStore
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:28 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ChangeRunnerStateToUpOperationProvider(
    private val schedulerRunnerStore: SchedulerRunnerStore,
) {

    val changeRunnerStateToUp = operationBuilder<RunnerKeyDto, Unit> {
        install(CheckRegisteredRole(schedulerRunnerStore.storeQuery))
        mainOperation {
            with(schedulerRunnerStore) {
                val schedulerRunnerDomain = (storeQuery.findByRunnerKeyAndServerKey(runnerKey, serverKey)
                    ?: throw RunnerErrors.RUNNER_IS_NOT_REGISTERED.unprocessableEntity())
                storeUpdater(schedulerRunnerDomain.identityUpdater()) {
                    InstanceState.UP.instanceState()
                    LocalDateTime.now().lastHeartbeatTime()
                    update()
                }.save()
            }
        }
    }

}
