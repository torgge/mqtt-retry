package com.bonespirito.configuration.async

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method

class CustomAsyncExceptionHandler : AsyncUncaughtExceptionHandler {
    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any?) {
        System.out.println("Exception message - " + ex.message)
        System.out.println("Method name - " + method.name)

        params.forEach { println("Param - $it") }
    }
}
