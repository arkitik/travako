package io.arkitik.travako.entity.server

import io.arkitik.travako.core.domain.server.ServerDomain
import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:19 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(
            name = "travako_server_key_unique",
            columnNames = [
                "serverKey"
            ]
        )
    ]
)
data class TravakoServer(
    @Id
    override val uuid: String,
    @Column(nullable = false, updatable = false)
    override val serverKey: String,
    @Column(nullable = false, updatable = false)
    override val creationDate: LocalDateTime = LocalDateTime.now(),
) : ServerDomain
