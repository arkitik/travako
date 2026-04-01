package io.arkitik.travako.sdk.domain.job.dto

import io.arkitik.travako.core.domain.server.ServerDomain
import java.time.LocalDate

/**
 * @author Ibrahim Al-Tamimi 
 * @since 11:21, Tuesday, 31/03/2026
 **/
data class JobCleanupDto(
    val beforeDate: LocalDate,
    val server: ServerDomain,
)