package io.arkitik.travako.operation.job

import io.arkitik.travako.operation.job.operation.FetchJobInstanceOperationProvider
import io.arkitik.travako.sdk.domain.job.JobDomainSdk
import io.arkitik.travako.store.job.JobInstanceStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:49 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobDomainSdkImpl(
    jobInstanceStore: JobInstanceStore,
) : JobDomainSdk {
    override val fetchJobInstance = FetchJobInstanceOperationProvider(
        jobInstanceStoreQuery = jobInstanceStore.storeQuery,
    ).fetchJobInstance
}
