package io.arkitik.travako.deploy.instance1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 10:23 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@SpringBootApplication
@EnableScheduling
class TravakoAppInstance1

fun main(args: Array<String>) {
    runApplication<TravakoAppInstance1>(*args)
}
