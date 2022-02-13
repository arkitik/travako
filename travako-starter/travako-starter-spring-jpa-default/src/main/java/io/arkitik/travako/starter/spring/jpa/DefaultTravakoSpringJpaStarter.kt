package io.arkitik.travako.starter.spring.jpa

import io.arkitik.travako.adapter.leader.LeaderPortContext
import io.arkitik.travako.port.job.JobInstancePortContext
import io.arkitik.travako.port.job.event.JobEventPortContext
import io.arkitik.travako.port.runner.RunnerPortContext
import io.arkitik.travako.port.server.ServerPortContext
import io.arkitik.travako.port.shared.SharedPortContext
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:41 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
@Import(value = [
    SharedPortContext::class,
    RunnerPortContext::class,
    JobInstancePortContext::class,
    ServerPortContext::class,
    LeaderPortContext::class,
    JobEventPortContext::class,
])
@EntityScan(
    value = [
        "io.arkitik.travako.entity.runner",
        "io.arkitik.travako.entity.job",
        "io.arkitik.travako.entity.job.event",
        "io.arkitik.travako.entity.server",
        "io.arkitik.travako.entity.leader",
    ]
)
@EnableJpaRepositories(
    value = [
        "io.arkitik.travako.adapter.runner.repository",
        "io.arkitik.travako.adapter.job.repository",
        "io.arkitik.travako.adapter.job.event.repository",
        "io.arkitik.travako.adapter.server.repository",
        "io.arkitik.travako.adapter.leader.repository",
    ]
)
class DefaultTravakoSpringJpaStarter
