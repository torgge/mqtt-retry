package com.bonespirito.mqttretry.application

import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.config.CONSUMER_ID
import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.utils.ConsumerUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class ConsumerService(
    @Autowired private val consumerUtils: ConsumerUtils
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun startConsume() {
        log.info("\nTrying to start consume queue: $CONSUMER_ID\n")
        consumerUtils.startConsumer(CONSUMER_ID)
    }

    fun stopConsume() {
        consumerUtils.stopConsumer(CONSUMER_ID)
    }

    @Async("taskExecutor")
    fun asyncConsume() {
        try {
            log.info("##START ASYNC CONSUME##")
            this.startConsume()
            20000.also { Thread.sleep(it.toLong()) }
            this.stopConsume()
        } catch (e: Error) {
            this.stopConsume()
        }
        log.info("##STOPPED ASYNC CONSUME##")
    }
}
