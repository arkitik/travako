package io.arkitik.travako.core.domain.job

import io.arkitik.radix.develop.identity.Identity

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:09 PM, 26/08/2024
 */
interface JobInstanceParamDomain : Identity<String> {
    override val uuid: String
    val job: JobInstanceDomain
    val key: String
    val value: String?
}
