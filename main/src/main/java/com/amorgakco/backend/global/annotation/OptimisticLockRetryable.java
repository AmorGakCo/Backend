package com.amorgakco.backend.global.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 4, backoff =
@Backoff(delay = 300, maxDelay = 600, multiplier = 1.2, random = true)
)
public @interface OptimisticLockRetryable {

    @AliasFor(annotation = Retryable.class, attribute = "recover")
    String recover() default "";

}
