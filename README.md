# travako 2.4.0-BETA

-----

## How to use **travako**?

-----

### To use Travako, follow these steps:

#### (1) Include below dependency in your module (Scheduler Module):

```xml
<dependency>
  <groupId>io.arkitik.travako</groupId>
  <artifactId>travako-starter-job-bean</artifactId>
  <version>${arkitik.travako.version}</version>
</dependency>
```

#### (2) Implement interface `JobInstanceBean` as a sample as the following:

```kotlin
internal class SampleJob: JobInstanceBean {

  override val trigger = PeriodicTrigger(10_000)  // or CronTrigger("*/10 * * * * *")
  override val jobKey: String = javaClass.simpleName // should be unique per application

  override fun runJob() {
    // Do the processing here.
  }
}
```

#### (3) Create a configuration class that defines the job as a bean:

```kotlin
@Configuration
class SampleJobPortContext {
  @Bean
  fun sampleJob(): JobInstanceBean = SampleJob()
}
```

#### (4) Add the following dependency to your `pom.xml` file to enable Travako::

```xml

<dependency>
  <groupId>io.arkitik.travako</groupId>
  <artifactId>travako-starter-processor</artifactId>
  <version>${arkitik.travako.version}</version>
</dependency>
```

#### (5) Define the configuration properties for Travako in your `application.yml` file:

```yaml
arkitik:
  travako:
    config:
      server-key: travako-server
      runner-key: travako-runner-1
      duplication-processor: false
      heartbeat: 5s
      jobs-assignee: 1m
      jobs-event: 30s
      leader-switch: 1h
```

#### (6) Choose one of the following dependencies to configure the data source for Travako::

* Default Spring `entityManagerFactory` option:

  ```xml
  
  <dependency>
    <groupId>io.arkitik.travako</groupId>
    <artifactId>travako-starter-spring-jpa-default</artifactId>
    <version>${arkitik.travako.version}</version>
  </dependency>
  ```

* Custom Spring `entityManagerFactory` option.

  ```xml
  
  <dependency>
    <groupId>io.arkitik.travako</groupId>
    <artifactId>travako-starter-spring-jpa-custom</artifactId>
    <version>${arkitik.travako.version}</version>
  </dependency>
  ```

##### If you choose the custom Spring `entityManagerFactory`, you also need to add the following configuration to your `application.yml`:

```yaml
arkitik:
  travako:
    database:
      datasource:
        url: ${database_url:jdbc:mysql://localhost:3307/travako}
        username: ${database_user:travako}
        password: ${database_password:travako}
        driver-class-name: ${database_driverClass:com.mysql.cj.jdbc.Driver}
```

### TODO:

> Add more details about how to use _**travako**_ service.
