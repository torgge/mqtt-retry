package com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.consumer

import com.bonespirito.mqttretry.domain.message.MessageConsumer
import com.bonespirito.mqttretry.domain.message.MessageProducer
import com.bonespirito.mqttretry.domain.payload.OrderPayloadRequest
import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.config.CONSUMER_ID
import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.config.MAX_RETRIES
import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.config.X_EXECUTE_AFTER
import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.config.X_RETRIES
import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.utils.allowProcessMessage
import com.rabbitmq.client.Channel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class OrderMessageConsumer(
    @Autowired private val messageProducer: MessageProducer
) : MessageConsumer {

    private val log = LoggerFactory.getLogger(javaClass)

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    @RabbitListener(
        id = CONSUMER_ID,
        queues = ["\${spring.rabbitmq.order.dlq-queue}"],
        autoStartup = "false",
        ackMode = "MANUAL"
    )
    override fun consume(message: Message, channel: Channel) {
        Thread.sleep(30000L) // This is only a test
        val tag = message.messageProperties.deliveryTag
        val body: OrderPayloadRequest = json.decodeFromString(String(message.body))
        val retries = message.messageProperties.headers[X_RETRIES] as Int + 1
        val mustExecuteAfter = message.messageProperties.headers[X_EXECUTE_AFTER] as Date

        log.info("### received message from message TAG: $tag ###")

        if (allowProcessMessage(mustExecuteAfter)) {
            if (retries < MAX_RETRIES) {
                messageProducer.produce(body, retries)
//                log.info("*** RETRY *** \n bodyVO: ${body.toVO()} \n retry: $retries")
                log.info("*** RETRY *** \n TAG: $tag \n retry: $retries")
            } else {
//                log.info("*** MAX RETRIES ARE EXCEEDED *** \n bodyVO: ${body.toVO()} \n retry: $retries")
                log.info("*** MAX RETRIES ARE EXCEEDED *** \n TAG: $tag \n retry: $retries")
            }
            channel.basicAck(tag, false)
        } else {
            log.info("*** NO READY TO CONSUME *** TAG: $tag")
            channel.basicNack(tag, false, true)
        }
    }
}
