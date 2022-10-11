package com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.producer

import com.bonespirito.mqttretry.domain.message.MessageProducer
import com.bonespirito.mqttretry.domain.payload.MessagePayload
import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.config.X_EXECUTE_AFTER
import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.config.X_RETRIES
import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.utils.getRetryDate
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

    @Value("\${spring.rabbitmq.order.dlq-exchange}")
    val dlqExchange: String = ""

    @Value("\${spring.rabbitmq.order.dlq-routing-key}")
    val dlqRoutingKey: String = ""

    override fun produce(payload: MessagePayload, retry: Int) {
        log.info("publishing message: $payload")
        this.rabbitTemplate.convertAndSend(exchange, routingKey, payload) { m ->
            m.messageProperties.headers[X_RETRIES] = retry
            m.messageProperties.headers[X_EXECUTE_AFTER] = getRetryDate()
            m
        }
        log.info("### published payload: $payload ###")
    }

    override fun dlqProduce(payload: MessagePayload, retry: Int) {
        log.info("publishing dlq message: $payload")
        this.rabbitTemplate.convertAndSend(dlqExchange, dlqRoutingKey, payload) { m ->
            m.messageProperties.headers[X_RETRIES] = retry
            m.messageProperties.headers[X_EXECUTE_AFTER] = getRetryDate()
            m
        }
        log.info("### published payload: $payload ###")
    }
}
