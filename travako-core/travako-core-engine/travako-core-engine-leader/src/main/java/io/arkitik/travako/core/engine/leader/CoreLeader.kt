package io.arkitik.travako.core.engine.leader

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 8:21 PM, 04 , **Mon, July 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */

/**
 * JOB: Manage jobs and delegate them to the existing runners
 */
class CoreLeader(
    private val leaderConfig: LeaderConfig,
) {
    data class LeaderConfig(
        val serverKey: String,
        val runnerKey: String,
        val runnerHost: String,
    )

    private val runners = arrayListOf<CoreRunner>()
    private val jobs = arrayListOf<CoreJob>()

    fun registerRunner(runner: CoreRunner) {
        require(runner in runners) {
            "Runner Already registered"
        }
        this.runners.add(runner)
    }

    fun registerJob(job: CoreJob) {
        require(job in jobs) {
            "Runner Already registered"
        }
        this.jobs.add(job)
    }



}
