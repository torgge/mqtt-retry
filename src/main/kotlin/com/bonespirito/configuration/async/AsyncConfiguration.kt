package com.bonespirito.configuration.async

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@ComponentScan(
    "com.bonespirito.configuration",
    "com.bonespirito.mqttretry.infrastructure",
    "com.bonespirito.mqttretry.application"
)
class AsyncConfiguration : AsyncConfigurer {
    override fun getAsyncExecutor(): Executor? {
        val executor = ThreadPoolTaskExecutor()
        7.also { executor.corePoolSize = it }
        42.also { executor.maxPoolSize = it }
        11.also { executor.queueCapacity = it }
        executor.setThreadNamePrefix("MyExecutor-")
        executor.initialize()
        return executor
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? {
        return CustomAsyncExceptionHandler()
    }
}
