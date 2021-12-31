package io.arkitik.travako.store.server.updater

import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import io.arkitik.travako.core.domain.server.ServerDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:40 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface ServerUpdater : StoreIdentityUpdater<String, ServerDomain>
