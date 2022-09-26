package com.bonespirito.mqttretry.domain.message

import org.springframework.amqp.core.Message

interface MessageConsumer {
    fun consume(message: Message)
}
