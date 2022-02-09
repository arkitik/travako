package io.arkitik.travako.adapter.runner.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.server.TravakoServer
import io.arkitik.travako.store.runner.creator.SchedulerRunnerCreator
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:20 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SchedulerRunnerCreatorImpl : SchedulerRunnerCreator {
    private lateinit var instanceKey: String
    private lateinit var runnerHost: String
    private lateinit var server: ServerDomain
    private var instanceState: InstanceState = InstanceState.UP
    private var uuid: String = UUID.randomUUID().toString().replace("-", "")

    override fun String.runnerKey(): SchedulerRunnerCreator {
        instanceKey = this
        return this@SchedulerRunnerCreatorImpl
    }

    override fun String.runnerHost(): SchedulerRunnerCreator {
        runnerHost = this
        return this@SchedulerRunnerCreatorImpl
    }

    override fun InstanceState.instanceState(): SchedulerRunnerCreator {
        instanceState = this
        return this@SchedulerRunnerCreatorImpl
    }

    override fun ServerDomain.server(): SchedulerRunnerCreator {
        server = this
        return this@SchedulerRunnerCreatorImpl
    }

    override fun String.uuid(): StoreIdentityCreator<String, SchedulerRunnerDomain> {
        uuid = this
        return this@SchedulerRunnerCreatorImpl
    }

    override fun create() =
        TravakoSchedulerRunner(
            runnerKey = instanceKey,
            instanceState = instanceState,
            uuid = uuid,
            server = server as TravakoServer,
            runnerHost = runnerHost
        )
}
