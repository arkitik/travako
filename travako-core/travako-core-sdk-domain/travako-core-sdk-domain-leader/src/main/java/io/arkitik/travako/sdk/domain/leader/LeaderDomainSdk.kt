package io.arkitik.travako.sdk.domain.leader

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.sdk.domain.leader.dto.LeaderDomainServerDto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:09 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface LeaderDomainSdk {
    val fetchServerLeader: Operation<LeaderDomainServerDto, LeaderDomain>
}
