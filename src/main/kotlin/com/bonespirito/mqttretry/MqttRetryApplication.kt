package com.bonespirito.mqttretry

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class MqttRetryApplication

private val log = LoggerFactory.getLogger(MqttRetryApplication::class.java)

fun main(args: Array<String>) {
    log.info("\n ### STARTING APPLICATION ###")
    runApplication<MqttRetryApplication>(*args)
    log.info("\n ### APPLICATION WAS STARTED ###")
}
