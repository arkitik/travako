package io.arkitik.travako.starter.processor.scheduler

import org.springframework.scheduling.Trigger
import org.springframework.scheduling.TriggerContext
import java.time.Instant

/**
 * @author Ibrahim Al-Tamimi 
 * @since 13:19, Friday, 25/09/2026
 **/
internal class FirstExecutionTrigger(
    private val delegate: Trigger,
    private val firstExecution: Instant?,
) : Trigger {
    override fun nextExecution(triggerContext: TriggerContext): Instant? {
        if (triggerContext.lastScheduledExecution() == null &&
            firstExecution != null &&
            firstExecution.isAfter(triggerContext.clock.instant())
        ) {
            return firstExecution
        }
        return delegate.nextExecution(triggerContext)
    }
}
