package com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.utils

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ConsumerUtils(
    @Autowired private val registry: RabbitListenerEndpointRegistry
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Throws(Exception::class)
    fun startConsumer(consumerId: String) {
        val container = registry.getListenerContainer(consumerId)

        if (!container.isRunning) {
            container.start()
            log.info("\n $consumerId was started\n")
        }
    }

    @Throws(Exception::class)
    fun stopConsumer(consumerId: String) {
        val container = registry.getListenerContainer(consumerId)

        if (container.isRunning) {
            container.stop()
            log.info("\n $consumerId was stopped\n")
        }
    }
}
