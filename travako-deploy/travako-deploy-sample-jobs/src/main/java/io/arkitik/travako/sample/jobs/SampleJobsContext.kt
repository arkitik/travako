package io.arkitik.travako.sample.jobs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 31 3:43 PM, **Fri, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class SampleJobsContext {
    @Bean
    fun job0() = Job0()
    @Bean
    fun job4() = Job4()
    @Bean
    fun job2() = Job2()
    @Bean
    fun job1() = Job1()

    @Bean
    fun job3() = Job3()
}
