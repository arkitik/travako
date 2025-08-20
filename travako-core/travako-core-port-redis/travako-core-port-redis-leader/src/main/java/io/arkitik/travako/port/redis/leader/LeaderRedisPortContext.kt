package io.arkitik.travako.port.redis.leader

import io.arkitik.travako.adapter.redis.leader.LeaderStoreImpl
import io.arkitik.travako.adapter.redis.leader.repository.TravakoLeaderRepository
import io.arkitik.travako.store.leader.LeaderStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 4:50 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class LeaderRedisPortContext {
    @Bean
    fun leaderStore(
        travakoLeaderRepository: TravakoLeaderRepository,
    ): LeaderStore = LeaderStoreImpl(travakoLeaderRepository = travakoLeaderRepository)
}