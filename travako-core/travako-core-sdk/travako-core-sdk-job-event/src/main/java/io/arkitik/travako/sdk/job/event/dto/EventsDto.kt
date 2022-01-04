package io.arkitik.travako.sdk.job.event.dto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 4:15 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class EventsDto(
    val events: List<EventDataDto>,
)

data class EventDataDto(
    val eventUuid: String,
    val jobKey: String,
    val eventType: String,
)
