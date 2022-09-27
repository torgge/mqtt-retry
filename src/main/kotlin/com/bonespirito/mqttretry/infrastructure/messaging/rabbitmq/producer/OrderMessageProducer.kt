package com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.producer

import com.bonespirito.mqttretry.domain.message.MessageProducer
import com.bonespirito.mqttretry.domain.payload.MessagePayload
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class OrderMessageProducer(
    @Autowired private val rabbitTemplate: RabbitTemplate
) : MessageProducer {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${spring.rabbitmq.order.exchange}")
    val exchange: String = ""

    @Value("\${spring.rabbitmq.order.routing-key}")
    val routingKey: String = ""

    override fun produce(payload: MessagePayload, retry: Int) {
        log.info("publishing message: $payload")
        this.rabbitTemplate.convertAndSend(exchange, routingKey, payload) { m ->
            m.messageProperties.headers["x-retry"] = retry
            m
        }
        log.info("### published payload: $payload ###")
    }
}
