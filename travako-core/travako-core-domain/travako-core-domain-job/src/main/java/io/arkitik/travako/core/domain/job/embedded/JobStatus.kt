package io.arkitik.travako.core.domain.job.embedded

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 7:02 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
enum class JobStatus(
    private val repeatable: Boolean,
) {
    RUNNING(true),
    WAITING(true),
    DOWN(false),
    DONE(false);

    companion object {
        fun repeatable() = JobStatus.entries.filter { it.repeatable }
    }
}
