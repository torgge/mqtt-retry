package com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.consumer

import com.bonespirito.mqttretry.domain.message.MessageConsumer
import com.bonespirito.mqttretry.domain.payload.OrderPayloadRequest
import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.config.CONSUMER_ID
import com.bonespirito.mqttretry.utils.toVO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class OrderMessageConsumer : MessageConsumer {

    private val log = LoggerFactory.getLogger(javaClass)

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    @RabbitListener(
        id = CONSUMER_ID,
        queues = ["\${spring.rabbitmq.order.queue}"],
        autoStartup = "false"
    )
    override fun consume(message: Message) {
        log.info("###received message from message $message")
        val body: OrderPayloadRequest = json.decodeFromString(String(message.body))
        log.info("body: $body \n bodyVO: ${body.toVO()}")
    }
}
