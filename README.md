# travako

## How to use **travako**?

* include below dependency in your module (Scheduler Module):

```xml

<dependency>
    <groupId>io.arkitik.travako</groupId>
    <artifactId>travako-starter-job-bean</artifactId>
    <version>_travako-latest-version_</version>
</dependency>

```

* Implement interface `JobInstanceBean` as sample of follows:

```kotlin
class Job0 : JobInstanceBean {
    override val trigger = PeriodicTrigger(10_000) // or CronTrigger("*/10 * * * * *")
    override val jobKey: String = javaClass.simpleName // should be unique per application, so; 

    private val logger = logger<Job0>()

    override fun runJob() {
        // JOB command
    }
}
```

* Jobs bean will be scanned by Spring framework, so; a bean should be defined for the required JOB on Spring context.

### TODO:
>   Add more details about how to use _**travako**_ service.
