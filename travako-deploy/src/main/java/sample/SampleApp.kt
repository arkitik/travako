package sample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 9:04 PM, 19/08/2025
 */
@SpringBootApplication
@EnableScheduling
class SampleApp

fun main(args: Array<String>) {
    runApplication<SampleApp>(*args)
}