package io.arkitik.travako.sdk.domain.job.event

import io.arkitik.radix.develop.operation.Operator
import io.arkitik.travako.core.domain.server.ServerDomain

/**
 * @author Ibrahim Al-Tamimi 
 * @since 16:57, Tuesday, 31/03/2026
 **/
interface JobEventDomainSdk {
    val cleanupDoneJobEvents: Operator<ServerDomain, Unit>

    val cleanupJobEventStatesForDownRunners: Operator<ServerDomain, Unit>
}