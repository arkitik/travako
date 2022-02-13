package io.arkitik.travako.starter.processor.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 10:03 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
inline fun <reified T> logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}
