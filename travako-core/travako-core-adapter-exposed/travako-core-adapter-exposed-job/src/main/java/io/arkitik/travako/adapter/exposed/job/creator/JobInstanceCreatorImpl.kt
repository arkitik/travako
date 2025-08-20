package io.arkitik.travako.adapter.exposed.job.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.exposed.job.TravakoJobInstance
import io.arkitik.travako.entity.exposed.job.TravakoJobInstanceTable
import io.arkitik.travako.store.job.creator.JobInstanceCreator
import org.jetbrains.exposed.sql.Database
import java.time.LocalDateTime
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:20 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class JobInstanceCreatorImpl(
    private val identityTable: TravakoJobInstanceTable,
    private val database: Database? = null,
) : JobInstanceCreator {
    private lateinit var jobKey: String
    private lateinit var jobClassName: String
    private lateinit var server: ServerDomain
    private lateinit var jobTrigger: String
    private lateinit var jobTriggerType: JobInstanceTriggerType
    private var nextExecutionTime: LocalDateTime? = null

    private var jobStatus: JobStatus = JobStatus.WAITING
    private var uuid: String = UUID.randomUUID().toString().replace("-", "")
    private var singleRun: Boolean = false
    private var assignedTo: SchedulerRunnerDomain? = null

    override fun String.jobKey(): JobInstanceCreator {
        this@JobInstanceCreatorImpl.jobKey = this
        return this@JobInstanceCreatorImpl
    }

    override fun String.jobClassName(): JobInstanceCreator {
        this@JobInstanceCreatorImpl.jobClassName = this
        return this@JobInstanceCreatorImpl
    }

    override fun JobStatus.jobStatus(): JobInstanceCreator {
        this@JobInstanceCreatorImpl.jobStatus = this
        return this@JobInstanceCreatorImpl
    }

    override fun String.jobTrigger(): JobInstanceCreator {
        this@JobInstanceCreatorImpl.jobTrigger = this
        return this@JobInstanceCreatorImpl
    }

    override fun JobInstanceTriggerType.jobTriggerType(): JobInstanceCreator {
        this@JobInstanceCreatorImpl.jobTriggerType = this
        return this@JobInstanceCreatorImpl
    }

    override fun Boolean.singleRun(): JobInstanceCreator {
        this@JobInstanceCreatorImpl.singleRun = this
        return this@JobInstanceCreatorImpl
    }

    override fun LocalDateTime?.nextExecutionTime(): JobInstanceCreator {
        this@JobInstanceCreatorImpl.nextExecutionTime = this
        return this@JobInstanceCreatorImpl
    }

    override fun String.uuid(): StoreIdentityCreator<String, JobInstanceDomain> {
        this@JobInstanceCreatorImpl.uuid = this
        return this@JobInstanceCreatorImpl
    }

    override fun ServerDomain.server(): JobInstanceCreator {
        this@JobInstanceCreatorImpl.server = this
        return this@JobInstanceCreatorImpl
    }

    override fun create(): JobInstanceDomain =
        TravakoJobInstance(
            jobKey = jobKey,
            jobClassName = jobClassName,
            jobStatus = jobStatus,
            serverUuid = server.uuid,
            serverTable = identityTable.serverTable,
            jobTrigger = jobTrigger,
            jobTriggerType = jobTriggerType,
            assignedToUuid = assignedTo?.uuid,
            runnerTable = identityTable.runnerTable,
            database = database,
            nextExecutionTime = nextExecutionTime,
            singleRun = singleRun,
            uuid = uuid,
            creationDate = LocalDateTime.now(),
            lastRunningTime = null,
        )
}
