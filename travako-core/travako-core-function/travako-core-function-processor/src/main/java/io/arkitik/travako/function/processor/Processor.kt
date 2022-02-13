package io.arkitik.travako.function.processor

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 9:00 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface Processor<T : Any> {
    val type: Class<T>
    fun process()
}
