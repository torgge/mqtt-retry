package com.bonespirito.mqttretry.domain.message

import com.bonespirito.mqttretry.domain.payload.MessagePayload

interface MessageProducer {
    fun produce(payload: MessagePayload, retry: Int)
    fun dlqProduce(payload: MessagePayload, retry: Int)
}
