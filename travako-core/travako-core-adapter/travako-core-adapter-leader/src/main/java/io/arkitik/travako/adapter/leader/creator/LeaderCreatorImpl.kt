package io.arkitik.travako.adapter.leader.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.leader.TravakoLeader
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.server.TravakoServer
import io.arkitik.travako.store.leader.creator.LeaderCreator
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:57 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class LeaderCreatorImpl : LeaderCreator {
    private lateinit var server: ServerDomain
    private lateinit var runner: SchedulerRunnerDomain
    private var uuid: String = UUID.randomUUID().toString().replace("-", "")

    override fun ServerDomain.server(): LeaderCreator {
        server = this
        return this@LeaderCreatorImpl
    }

    override fun SchedulerRunnerDomain.runner(): LeaderCreator {
        runner = this
        return this@LeaderCreatorImpl
    }

    override fun String.uuid(): StoreIdentityCreator<String, LeaderDomain> {
        uuid = this
        return this@LeaderCreatorImpl
    }

    override fun create(): LeaderDomain {
        return TravakoLeader(
            uuid = uuid,
            server = server as TravakoServer,
            runner = runner as TravakoSchedulerRunner
        )
    }
}
