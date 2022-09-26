package com.bonespirito.mqttretry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MqttRetryApplication

fun main(args: Array<String>) {
	runApplication<MqttRetryApplication>(*args)
}
