package com.bonespirito.mqttretry.application

import com.bonespirito.mqttretry.domain.message.MessageProducer
import com.bonespirito.mqttretry.domain.model.Order
import com.bonespirito.mqttretry.domain.service.OrderFacade
import com.bonespirito.mqttretry.utils.toPayload
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrderFacadeImp(
    @Autowired private val messageProducer: MessageProducer
) : OrderFacade {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun process(order: Order) {
        log.info("\nTrying to process an order $order\n")
        val payload = order.toPayload()
        log.info("Trying to produce a message from payload $payload")
        messageProducer.produce(payload)
    }
}
