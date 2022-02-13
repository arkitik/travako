package io.arkitik.travako.sdk.server

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.travako.sdk.server.dto.ServerKeyDto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:34 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface ServerSdk {
    val registerServer: Operation<ServerKeyDto, Unit>

    val isServerRegistered: OperationRole<ServerKeyDto, Boolean>
}
