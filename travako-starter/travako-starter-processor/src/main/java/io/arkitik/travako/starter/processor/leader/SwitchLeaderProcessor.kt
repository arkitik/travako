package io.arkitik.travako.starter.processor.leader

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.leader.LeaderSdk
import io.arkitik.travako.sdk.leader.dto.IsLeaderBeforeDto
import io.arkitik.travako.sdk.leader.dto.LeaderServerKeyDto
import io.arkitik.travako.starter.processor.config.TravakoConfig
import io.arkitik.travako.starter.processor.logger.logger
import io.arkitik.travako.starter.processor.scheduler.fixedRateJob
import org.springframework.scheduling.TaskScheduler
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 10:02 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SwitchLeaderProcessor(
    private val travakoConfig: TravakoConfig,
    private val taskScheduler: TaskScheduler,
    private val leaderSdk: LeaderSdk,
    private val transactionalExecutor: TransactionalExecutor,
) : Processor<LeaderDomain> {
    override val type = LeaderDomain::class.java

    private val logger = logger<SwitchLeaderProcessor>()

    override fun process() {
        travakoConfig.leaderSwitch
            .fixedRateJob(taskScheduler) {
                transactionalExecutor.runOnTransaction {
                    leaderSdk.isLeaderBefore
                        .operateRole(IsLeaderBeforeDto(
                            serverKey = travakoConfig.serverKey,
                            runnerKey = travakoConfig.keyDto.runnerKey,
                            runnerHost = travakoConfig.keyDto.runnerHost,
                            dateBefore = LocalDateTime.now().minus(travakoConfig.leaderSwitch)
                        )).takeIf { it }?.let {
                            logger.debug(
                                "Start moving {} leader processor responsibilities from runner {}.",
                                travakoConfig.serverKey,
                                travakoConfig.runnerKey
                            )
                            val response = leaderSdk.switchLeader
                                .runOperation(LeaderServerKeyDto(travakoConfig.serverKey))
                            logger.debug(
                                "{} has been promoted to be {} leader for the next running period",
                                "${response.runnerKey}-${response.runnerHost}",
                                response.serverKey,
                            )
                        }
                }
            }
    }
}
