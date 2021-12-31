package io.arkitik.travako.sdk.leader

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.travako.sdk.leader.dto.IsLeaderBeforeDto
import io.arkitik.travako.sdk.leader.dto.LeaderRunnerDetails
import io.arkitik.travako.sdk.leader.dto.LeaderRunnerKeyDto
import io.arkitik.travako.sdk.leader.dto.LeaderServerKeyDto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 7:49 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface LeaderSdk {
    val registerLeaderServer: Operation<LeaderRunnerKeyDto, Unit>
    val switchLeader: Operation<LeaderServerKeyDto, LeaderRunnerKeyDto>

    val isLeaderBefore: OperationRole<IsLeaderBeforeDto, Boolean>

    val currentLeader: Operation<LeaderServerKeyDto, LeaderRunnerDetails>
}
