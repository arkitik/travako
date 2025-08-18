package io.arkitik.travako.adapter.exposed.job.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.JobInstanceParamDomain
import io.arkitik.travako.entity.exposed.job.TravakoJobInstanceParam
import io.arkitik.travako.entity.exposed.job.TravakoJobInstanceParamTable
import io.arkitik.travako.store.job.creator.JobInstanceParamCreator
import org.jetbrains.exposed.sql.Database
import java.time.LocalDateTime
import java.util.*

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:27 PM, 26/08/2024
 */
internal class JobInstanceParamCreatorImpl(
    private val identityTable: TravakoJobInstanceParamTable,
    private val database: Database? = null,
) : JobInstanceParamCreator {
    private lateinit var job: JobInstanceDomain
    private lateinit var key: String
    private var value: String? = null
    private var uuid: String = UUID.randomUUID().toString().replace("-", "")

    override fun JobInstanceDomain.job(): JobInstanceParamCreator {
        this@JobInstanceParamCreatorImpl.job = this
        return this@JobInstanceParamCreatorImpl
    }

    override fun String.key(): JobInstanceParamCreator {
        this@JobInstanceParamCreatorImpl.key = this
        return this@JobInstanceParamCreatorImpl
    }

    override fun String?.value(): JobInstanceParamCreator {
        this@JobInstanceParamCreatorImpl.value = this
        return this@JobInstanceParamCreatorImpl
    }

    override fun String.uuid(): StoreIdentityCreator<String, JobInstanceParamDomain> {
        this@JobInstanceParamCreatorImpl.uuid = this
        return this@JobInstanceParamCreatorImpl
    }

    override fun create(): JobInstanceParamDomain {
        return TravakoJobInstanceParam(
            uuid = uuid,
            jobUuid = job.uuid,
            jobInstanceTable = identityTable.jobInstanceTable,
            database = database,
            key = key,
            value = value,
            creationDate = LocalDateTime.now()
        )
    }
}
