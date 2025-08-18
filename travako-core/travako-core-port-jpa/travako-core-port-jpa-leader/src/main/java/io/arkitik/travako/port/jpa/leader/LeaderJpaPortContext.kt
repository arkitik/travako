package io.arkitik.travako.port.jpa.leader

import io.arkitik.travako.adapter.leader.LeaderStoreImpl
import io.arkitik.travako.adapter.leader.repository.TravakoLeaderRepository
import io.arkitik.travako.store.leader.LeaderStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 4:50 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class LeaderJpaPortContext {
    @Bean
    fun leaderStore(
        travakoLeaderRepository: TravakoLeaderRepository,
    ): LeaderStore = LeaderStoreImpl(travakoLeaderRepository = travakoLeaderRepository)
}